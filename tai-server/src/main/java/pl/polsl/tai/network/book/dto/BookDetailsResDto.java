package pl.polsl.tai.network.book.dto;

import pl.polsl.tai.domain.book.BookEntity;

import java.util.List;

public record BookDetailsResDto(
  String title,
  String isbn,
  int year,
  String publisher,
  String city,
  List<BookAuthorResDto> authors,
  long allCopies,
  long availableCopies
) {
  public BookDetailsResDto(BookEntity book, List<BookAuthorResDto> authors, long rented) {
    this(
      book.getTitle(),
      book.getIsbn(),
      book.getYear(),
      book.getPublisher(),
      book.getCity(),
      authors,
      book.getCopies(),
      book.getCopies() - rented
    );
  }
}
