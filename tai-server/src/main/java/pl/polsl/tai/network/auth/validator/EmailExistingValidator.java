package pl.polsl.tai.network.auth.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.polsl.tai.domain.user.UserRepository;

@Component
@RequiredArgsConstructor
public class EmailExistingValidator implements ConstraintValidator<EmailExists, String> {
	private final UserRepository userRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !userRepository.existsByEmail(value);
	}
}
