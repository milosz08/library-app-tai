package pl.polsl.tai.network.book.dto;

import pl.polsl.tai.domain.book.BookEntity;

public record BookRowResDto(long id, String title, long allCopies, long availableCopies) {
	public BookRowResDto(BookEntity book, long rented) {
		this(book.getId(), book.getTitle(), book.getCopies(), book.getCopies() - rented);
	}
}
