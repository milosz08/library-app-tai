package pl.polsl.tai.network.auth.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

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
