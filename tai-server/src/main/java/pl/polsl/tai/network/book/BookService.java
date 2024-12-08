package pl.polsl.tai.network.book;

import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.dto.DeleteResultResDto;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.network.book.dto.AddEditBookReqDto;
import pl.polsl.tai.network.book.dto.BookDetailsResDto;
import pl.polsl.tai.network.book.dto.BookRowResDto;
import pl.polsl.tai.security.LoggedUser;

import java.util.List;

interface BookService {
	PageableResDto<BookRowResDto, BookEntity> getPageableBooks(String title, Integer page, Integer size);

	BookDetailsResDto getBookDetails(Long bookId);

	void createBook(AddEditBookReqDto reqDto, LoggedUser loggedUser);

	void updateBook(Long bookId, AddEditBookReqDto reqDto, LoggedUser loggedUser);

	DeleteResultResDto deleteBooks(List<Long> bookIds, LoggedUser loggedUser);
}
