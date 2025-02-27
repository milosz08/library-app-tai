package pl.polsl.tai.network.rental;

import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.network.rental.dto.LoanBookReqDto;
import pl.polsl.tai.network.rental.dto.RentedBookDetailsResDto;
import pl.polsl.tai.network.rental.dto.RentedBookRowResDto;
import pl.polsl.tai.network.rental.dto.ReturnBookReqDto;
import pl.polsl.tai.security.LoggedUser;

interface RentalService {
  PageableResDto<RentedBookRowResDto, BookEntity> getPageableRentedBooks(String title, Integer page, Integer size, LoggedUser loggedUser);

  RentedBookDetailsResDto getRentedBookDetails(Long bookId, LoggedUser loggedUser);

  void loanBook(LoanBookReqDto reqDto, LoggedUser loggedUser);

  void returnBook(ReturnBookReqDto reqDto, LoggedUser loggedUser);
}
