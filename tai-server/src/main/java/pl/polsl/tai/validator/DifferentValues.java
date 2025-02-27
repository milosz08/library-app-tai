package pl.polsl.tai.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.polsl.tai.validator.constraint.ValuesDifferentValidator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValuesDifferentValidator.class)
@Documented
public @interface DifferentValues {
  String message();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
