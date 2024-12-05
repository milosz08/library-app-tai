package pl.polsl.tai.network.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.tai.util.Regex;
import pl.polsl.tai.validator.EmailExists;
import pl.polsl.tai.validator.MatchPasswords;

@Getter
@Setter
@MatchPasswords(message = "Hasło oraz potwierdzenie hasła nie są takie same.")
public class RegisterReqDto {
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

	@NotBlank(message = "Hasło jest wymagane.")
	@Size(max = 100, message = "Hasło może mieć maksymalnie 100 znaków.")
	@Pattern(regexp = Regex.PASSWORD_REQ, message = "Hasło musi zawierać co najmniej 8 znaków, jedną wielką literę, " +
		"jedną cyfrę i znak specjalny (spośród #?!@$%^&*).")
	private String password;

	@NotBlank(message = "Potwierdzenie hasła jest wymagane.")
	private String confirmedPassword;

	@NotBlank(message = "Ulica jest wymagana.")
	@Size(min = 2, max = 100, message = "Nazwa ulicy musi mieć od 2 do 100 znaków.")
	private String street;

	@NotBlank(message = "Numer budynku jest wymagany.")
	@Size(max = 10, message = "Numer budynku może mieć maksymalnie do 10 znaków.")
	private String buildingNumber;

	@Size(max = 10, message = "Numer mieszkania może mieć maksymalnie do 10 znaków.")
	private String apartmentNumber;

	@NotBlank(message = "Miasto jest wymagane.")
	@Size(min = 2, max = 100, message = "Miasto musi mieć od 2 do 100 znaków.")
	private String city;
}
