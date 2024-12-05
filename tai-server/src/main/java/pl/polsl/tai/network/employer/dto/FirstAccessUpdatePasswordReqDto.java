package pl.polsl.tai.network.employer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.tai.util.Regex;
import pl.polsl.tai.validator.DifferentValues;
import pl.polsl.tai.validator.DifferentValuesDto;
import pl.polsl.tai.validator.MatchValues;
import pl.polsl.tai.validator.MatchValuesDto;

import java.util.List;

@Getter
@Setter
@MatchValues(message = "Nowe hasło oraz potwierdzenie nowego hasła nie są takie same.")
@DifferentValues(message = "Tymczasowe hasło oraz nowe hasło nie powinny być takie same.")
public class FirstAccessUpdatePasswordReqDto implements MatchValuesDto, DifferentValuesDto {
	@NotNull(message = "Pole tymczasowego hasła musi istnieć.")
	private String temporaryPassword;

	@NotBlank(message = "Nowe hasło jest wymagane.")
	@Size(max = 100, message = "Nowe Hasło może mieć maksymalnie 100 znaków.")
	@Pattern(regexp = Regex.PASSWORD_REQ, message = "Nowe hasło musi zawierać co najmniej 8 znaków, jedną wielką " +
		"literę, jedną cyfrę i znak specjalny (spośród #?!@$%^&*).")
	private String newPassword;

	@NotBlank(message = "Potwierdzenie nowego hasła jest wymagane.")
	private String confirmedNewPassword;

	@Override
	public List<String> getMatchValues() {
		return List.of(newPassword, confirmedNewPassword);
	}

	@Override
	public List<String> getDifferentValues() {
		return List.of(newPassword, temporaryPassword);
	}
}
