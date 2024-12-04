package pl.polsl.tai.network.auth.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailExistingValidator.class)
@Documented
public @interface EmailExists {
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
