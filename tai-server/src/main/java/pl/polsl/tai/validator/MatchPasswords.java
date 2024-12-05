package pl.polsl.tai.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.polsl.tai.validator.constraint.PasswordMatchValidator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
public @interface MatchPasswords {
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
