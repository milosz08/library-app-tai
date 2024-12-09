package pl.polsl.tai.network.rental.dto;

import pl.polsl.tai.domain.book.BookEntity;

public record RentedBookRowResDto(long id, String title, long rentedCopies) {
	public RentedBookRowResDto(BookEntity book, long rented) {
		this(book.getId(), book.getTitle(), rented);
	}
}
