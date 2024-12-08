package pl.polsl.tai.network.book.dto;

import pl.polsl.tai.domain.author.AuthorEntity;

public record BookAuthorResDto(String firstName, String lastName) {
	public BookAuthorResDto(AuthorEntity author) {
		this(author.getFirstName(), author.getLastName());
	}
}
