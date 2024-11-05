package pl.polsl.tai.network.auth.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;

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
