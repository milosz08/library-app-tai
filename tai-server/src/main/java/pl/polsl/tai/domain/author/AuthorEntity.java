package pl.polsl.tai.domain.author;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.book.BookEntity;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "authors")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthorEntity extends EntityBase implements Serializable {

	private String firstName;

	private String lastName;

	@ManyToOne
	@JoinColumn
	private BookEntity book;

	@Override
	public String toString() {
		return "{" +
			"firstName=" + firstName +
			", lastName=" + lastName +
			'}';
	}
}
