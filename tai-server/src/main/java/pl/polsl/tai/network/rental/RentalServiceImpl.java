package pl.polsl.tai.network.rental;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.domain.book.BookRepository;
import pl.polsl.tai.domain.book.BookSpec;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.exception.NotFoundRestServerException;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.network.book.dto.BookAuthorResDto;
import pl.polsl.tai.network.rental.dto.LoanBookReqDto;
import pl.polsl.tai.network.rental.dto.RentedBookDetailsResDto;
import pl.polsl.tai.network.rental.dto.RentedBookRowResDto;
import pl.polsl.tai.network.rental.dto.ReturnBookReqDto;
import pl.polsl.tai.security.LoggedUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
class RentalServiceImpl implements RentalService {
  private final LogPersistService logPersistService;

  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  @Override
  public PageableResDto<RentedBookRowResDto, BookEntity> getPageableRentedBooks(
    String title,
    Integer page,
    Integer size,
    LoggedUser loggedUser
  ) {
    final String titleSafe = title == null ? "" : title;
    final int pageSafe = page == null ? 1 : page;
    final int sizeSafe = size == null ? 10 : size;

    final Map<Long, Long> rentedUserBooks = new HashMap<>();
    for (final Tuple rented : bookRepository.findAllRentedForUser(loggedUser.userEntity().getId())) {
      rentedUserBooks.put(rented.get(0, Long.class), rented.get(1, Long.class));
    }
    final Specification<BookEntity> spec = Specification
      .where(BookSpec.hasTitle(titleSafe))
      .and(BookSpec.hasIds(rentedUserBooks.keySet()));

    final Page<BookEntity> pageable = bookRepository
      .findAll(spec, PageRequest.of(pageSafe - 1, sizeSafe));

    final List<RentedBookRowResDto> results = pageable
      .map(b -> new RentedBookRowResDto(b, rentedUserBooks.getOrDefault(b.getId(), 0L)))
      .toList();

    return new PageableResDto<>(results, pageable);
  }

  @Override
  public RentedBookDetailsResDto getRentedBookDetails(Long id, LoggedUser loggedUser) {
    final BookEntity book = findBook(id);
    final List<Long> userIds = book.getUsers().stream().map(EntityBase::getId).toList();
    if (!userIds.contains(loggedUser.userEntity().getId())) {
      throw new RestServerException("Nie znaleziono wypożyczonej książki.");
    }
    final List<BookAuthorResDto> authors = book.getAuthors().stream()
      .map(BookAuthorResDto::new)
      .toList();
    final long rentedCount = bookRepository.countAllRentedByUserId(id);

    return new RentedBookDetailsResDto(book, authors, rentedCount);
  }

  @Override
  @Transactional
  public void loanBook(LoanBookReqDto reqDto, LoggedUser loggedUser) {
    final BookEntity book = findBook(reqDto.getBookId());
    final long availableCopies = book.getCopies() - book.getUsers().size();
    if (reqDto.getCount() > availableCopies) {
      String message;
      if (availableCopies == 0) {
        message = "Wybrana książka jest niedostępna do wypożyczenia.";
      } else {
        message = String.format("W systemie nie znajduje się wybrana ilość książek do " +
          "wypożyczenia. Możliwa ilość książek do wypożyczenia: %d.", availableCopies);
      }
      throw new RestServerException(message);
    }
    final UserEntity user = userRepository
      .findUserAndFetchBooks(loggedUser.userEntity().getId())
      .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika."));

    for (long i = 0; i < reqDto.getCount(); i++) {
      book.attachUser(user);
    }
    log.info("Customer: {} loan book: {}.", loggedUser.userEntity(), book);
    logPersistService.info("Klient: %s wypożyczył książkę: %s.", loggedUser.getUsername(),
      book.getTitle());
  }

  @Override
  @Transactional
  public void returnBook(ReturnBookReqDto reqDto, LoggedUser loggedUser) {
    final Tuple rented = bookRepository
      .findRentedByUserIdAndBookId(loggedUser.userEntity().getId(), reqDto.getBookId())
      .orElseThrow(() -> new NotFoundRestServerException("Nie znaleziono książki."));

    final long count = rented.get(0, Long.class);
    if (reqDto.getCount() > count) {
      String message;
      if (count == 0) {
        message = "Nie znaleziono żadnych wybranych książek możliwych do zwrotu.";
      } else {
        message = String.format("Liczba wypożyczonych książek jest mniejsza od zadeklarowanej " +
          "liczby książek do wypożyczenia. Maksymalna liczba wypożyczonych książek: %d.", count);
      }
      throw new RestServerException(message);
    }
    final UserEntity user = userRepository
      .findUserAndFetchBooks(loggedUser.userEntity().getId())
      .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika."));

    final BookEntity book = rented.get(1, BookEntity.class);
    for (long i = 0; i < reqDto.getCount(); i++) {
      book.detachUser(user);
    }
    log.info("Customer: {} return book: {}.", loggedUser.userEntity(), book);
    logPersistService.info("Klient: %s oddał książkę: %s.", loggedUser.getUsername(),
      book.getTitle());
  }

  private BookEntity findBook(Long id) {
    return bookRepository
      .findById(id)
      .orElseThrow(() -> new NotFoundRestServerException("Nie znaleziono wybranej książki."));
  }
}
