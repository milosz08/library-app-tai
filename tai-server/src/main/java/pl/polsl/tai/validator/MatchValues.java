package pl.polsl.tai.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.polsl.tai.validator.constraint.ValuesMatchValidator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValuesMatchValidator.class)
@Documented
public @interface MatchValues {
  String message();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
