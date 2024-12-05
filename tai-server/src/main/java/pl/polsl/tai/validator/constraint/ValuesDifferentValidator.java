package pl.polsl.tai.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.polsl.tai.validator.DifferentValues;
import pl.polsl.tai.validator.DifferentValuesDto;

import java.util.List;
import java.util.Objects;

public class ValuesDifferentValidator implements ConstraintValidator<DifferentValues, DifferentValuesDto> {
	@Override
	public boolean isValid(DifferentValuesDto dto, ConstraintValidatorContext context) {
		final List<String> all = dto.getDifferentValues();
		if (all.stream().anyMatch(Objects::isNull)) {
			return false;
		}
		return all.stream().distinct().count() == all.size();
	}
}
