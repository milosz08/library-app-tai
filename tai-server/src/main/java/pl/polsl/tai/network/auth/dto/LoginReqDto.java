package pl.polsl.tai.network.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReqDto {
	@NotBlank(message = "Pole adresu email nie może być puste.")
	private String email;

	@NotBlank(message = "Pole hasła nie może być puste.")
	private String password;
}
