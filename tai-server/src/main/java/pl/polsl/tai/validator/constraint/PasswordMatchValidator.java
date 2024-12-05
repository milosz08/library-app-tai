package pl.polsl.tai.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.validator.MatchPasswords;

import java.util.Objects;

public class PasswordMatchValidator implements ConstraintValidator<MatchPasswords, RegisterReqDto> {
	@Override
	public boolean isValid(RegisterReqDto reqDto, ConstraintValidatorContext context) {
		if (reqDto.getPassword() != null && reqDto.getConfirmedPassword() != null) {
			return Objects.equals(reqDto.getPassword(), reqDto.getConfirmedPassword());
		}
		return false;
	}
}
