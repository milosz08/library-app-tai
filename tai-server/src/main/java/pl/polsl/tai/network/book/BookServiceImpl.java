package pl.polsl.tai.network.book;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.author.AuthorEntity;
import pl.polsl.tai.domain.author.AuthorRepository;
import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.domain.book.BookRepository;
import pl.polsl.tai.domain.book.BookSpec;
import pl.polsl.tai.dto.DeleteResultResDto;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.exception.NotFoundRestServerException;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.network.book.dto.*;
import pl.polsl.tai.security.LoggedUser;
import pl.polsl.tai.util.NaiveIsbnGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {
	private final LogPersistService logPersistService;

	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;

	@Override
	public PageableResDto<BookRowResDto, BookEntity> getPageableBooks(String title, Integer page, Integer size) {
		final String titleSafe = title == null ? "" : title;
		final int pageSafe = page == null ? 1 : page;
		final int sizeSafe = size == null ? 10 : size;

		final Specification<BookEntity> spec = Specification.where(BookSpec.hasTitle(titleSafe));
		final Page<BookEntity> pageable = bookRepository.findAll(spec, PageRequest.of(pageSafe - 1, sizeSafe));

		final Map<Long, Long> rentedBooks = new HashMap<>();
		for (final Tuple rented : bookRepository.findAllRented()) {
			rentedBooks.put(rented.get(0, Long.class), rented.get(1, Long.class));
		}
		final List<BookRowResDto> results = pageable
			.map(b -> new BookRowResDto(b, rentedBooks.getOrDefault(b.getId(), 0L)))
			.toList();
		return new PageableResDto<>(results, pageable);
	}

	@Override
	public BookDetailsResDto getBookDetails(Long bookId) {
		final BookEntity book = findBook(bookId);

		final List<BookAuthorResDto> authors = book.getAuthors().stream().map(BookAuthorResDto::new).toList();
		final long rentedCount = bookRepository.countAllRentedByUserId(bookId);

		return new BookDetailsResDto(book, authors, rentedCount);
	}

	@Override
	public void createBook(AddEditBookReqDto reqDto, LoggedUser loggedUser) {
		if (bookRepository.existsByTitle(reqDto.getTitle())) {
			throw new RestServerException("Książka o wybranym tytule istnieje już w systemie.");
		}
		final BookEntity book = new BookEntity();

		book.setTitle(reqDto.getTitle());
		book.setIsbn(NaiveIsbnGenerator.generateRandom());
		book.setYear(Integer.parseInt(reqDto.getYear()));
		book.setPublisher(reqDto.getPublisher());
		book.setCity(reqDto.getCity());
		book.setCopies(reqDto.getCopies());

		final Set<AuthorEntity> authors = toAuthorEntities(reqDto.getAuthors(), book);
		authors.forEach(book::attachAuthor);

		bookRepository.save(book);

		log.info("Create book: {} by: {}. Created book data: {}.", book.getTitle(), loggedUser.getUsername(), reqDto);
		logPersistService.info("Dodano książkę: %s przez pracownika: %s.", book.getTitle(), loggedUser.getUsername());
	}

	@Override
	@Transactional
	public void updateBook(Long bookId, AddEditBookReqDto reqDto, LoggedUser loggedUser) {
		final BookEntity book = findBook(bookId);

		book.setTitle(reqDto.getTitle());
		book.setYear(Integer.parseInt(reqDto.getYear()));
		book.setPublisher(reqDto.getPublisher());
		book.setCity(reqDto.getCity());
		book.setCopies(reqDto.getCopies());

		authorRepository.deleteAll(book.getAuthors());
		book.setAuthors(toAuthorEntities(reqDto.getAuthors(), book));

		log.info("Updated book: {} by: {}. Updated book data: {}.", book.getTitle(), loggedUser.getUsername(), reqDto);
		logPersistService.info("Zaktualizowano książkę: %s przez pracownika: %s.", book.getTitle(), loggedUser.getUsername());
	}

	@Override
	public DeleteResultResDto deleteBooks(List<Long> bookIds, LoggedUser loggedUser) {
		List<BookEntity> books;
		if (bookIds.isEmpty()) {
			books = bookRepository.findAllNotRented();
		} else {
			books = bookRepository.findAllNotRentedAndHasIds(bookIds);
		}
		final List<Long> dbIds = books.stream().map(EntityBase::getId).toList();
		final List<Long> diffIds = bookIds.stream().filter(l -> !dbIds.contains(l)).toList();

		if (!diffIds.isEmpty()) {
			final BookEntity firstBook = findBook(diffIds.get(0));
			throw new RestServerException(String.format(
				"Książka %s nie może zostać usunięta gdy jakikolwiek klient wypożycza ją.", firstBook.getTitle()));
		}
		bookRepository.deleteAll(books);

		final List<String> deletesBooks = books.stream().map(BookEntity::getTitle).toList();

		log.info("Delete book(s) by: {}. Deleted book(s) data: {}.", loggedUser.getUsername(), deletesBooks);
		logPersistService.info("Usunięto książkę/ki: %s przez pracownika: %s.", deletesBooks, loggedUser.getUsername());

		return new DeleteResultResDto(books.size());
	}

	private BookEntity findBook(Long bookId) {
		return bookRepository
			.findById(bookId)
			.orElseThrow(() -> new NotFoundRestServerException("Nie znaleziono wybranej książki."));
	}

	private Set<AuthorEntity> toAuthorEntities(List<BookAuthorReqDto> reqDto, BookEntity bookEntity) {
		return reqDto.stream()
			.map(a -> new AuthorEntity(a.getFirstName(), a.getLastName(), bookEntity))
			.collect(Collectors.toSet());
	}
}
