package pl.polsl.tai.network.employer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.tai.validator.EmailExists;

@Getter
@Setter
public class AddEmployerReqDto {
	@NotBlank(message = "Imię jest wymagane.")
	@Size(min = 2, max = 100, message = "Imię musi mieć od 2 do 100 znaków.")
	private String firstName;

	@NotBlank(message = "Nazwisko jest wymagane.")
	@Size(min = 2, max = 100, message = "Nazwisko musi mieć od 2 do 100 znaków.")
	private String lastName;

	@NotBlank(message = "Adres email jest wymagany.")
	@Size(max = 150, message = "Adres email może mieć maksymalnie 150 znaków.")
	@Email(message = "Adres email jest niepoprawny.")
	@EmailExists(message = "Podany adres email jest już zajęty.")
	private String email;
}
