package pl.polsl.tai.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.validator.EmailExists;

@Component
@RequiredArgsConstructor
public class EmailExistingValidator implements ConstraintValidator<EmailExists, String> {
	private final UserRepository userRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !userRepository.existsByEmail(value);
	}
}
