package pl.polsl.tai.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.polsl.tai.validator.MatchValues;
import pl.polsl.tai.validator.MatchValuesDto;

import java.util.List;
import java.util.Objects;

public class ValuesMatchValidator implements ConstraintValidator<MatchValues, MatchValuesDto> {
  @Override
  public boolean isValid(MatchValuesDto dto, ConstraintValidatorContext context) {
    final List<String> all = dto.getMatchValues();
    if (all.stream().anyMatch(Objects::isNull)) {
      return false;
    }
    return all.stream().allMatch(all.get(0)::equals);
  }
}
