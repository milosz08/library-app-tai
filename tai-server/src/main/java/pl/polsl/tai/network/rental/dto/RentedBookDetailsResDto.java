package pl.polsl.tai.network.rental.dto;

import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.network.book.dto.BookAuthorResDto;

import java.util.List;

public record RentedBookDetailsResDto(
	String title,
	String isbn,
	int year,
	String publisher,
	String city,
	List<BookAuthorResDto> authors,
	long rentedCopies
) {
	public RentedBookDetailsResDto(BookEntity book, List<BookAuthorResDto> authors, long rented) {
		this(
			book.getTitle(),
			book.getIsbn(),
			book.getYear(),
			book.getPublisher(),
			book.getCity(),
			authors,
			rented
		);
	}
}
