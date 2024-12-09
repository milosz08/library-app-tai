package pl.polsl.tai.network.book.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.tai.util.Regex;

import java.util.List;

@Getter
@Setter
public class AddEditBookReqDto {
	@NotBlank(message = "Tytuł jest wymagany.")
	@Size(min = 2, max = 255, message = "Tytuł musi mieć od 2 do 255 znaków.")
	private String title;

	@NotBlank(message = "Rok jest wymagany.")
	@Pattern(regexp = Regex.YEAR, message = "Rok musi być w formacie YYYY.")
	private String year;

	@NotBlank(message = "Nazwa wydawnictwa jest wymagana.")
	@Size(min = 2, max = 255, message = "Nazwa wydawnictwa mieć od 2 do 255 znaków.")
	private String publisher;

	@NotBlank(message = "Miasto jest wymagane.")
	@Size(min = 2, max = 100, message = "Nazwa miasta mieć od 2 do 100 znaków.")
	private String city;

	@NotNull(message = "Liczba kopii jest wymagana.")
	@Min(value = 1, message = "Liczba kopii musi być większa bądź równa 1.")
	private long copies;

	@Valid
	@NotNull(message = "Autorzy książki są wymagani.")
	@NotEmpty(message = "Należy podać przynajmniej jednego autora książki.")
	private List<BookAuthorReqDto> authors;

	@Override
	public String toString() {
		return "{" +
			"title=" + title +
			", year=" + year +
			", publisher=" + publisher +
			", city=" + city +
			", copies=" + copies +
			", authors=" + authors +
			'}';
	}
}
