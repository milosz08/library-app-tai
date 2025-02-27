package pl.polsl.tai.network.password.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.tai.util.Regex;
import pl.polsl.tai.validator.MatchValues;
import pl.polsl.tai.validator.MatchValuesDto;

import java.util.List;

@Getter
@Setter
@MatchValues(message = "Nowe hasło oraz potwierdzenie nowego hasła nie są takie same.")
public class ChangePasswordReqDto implements MatchValuesDto {
  @NotBlank(message = "Nowe hasło jest wymagane.")
  @Size(max = 100, message = "Nowe Hasło może mieć maksymalnie 100 znaków.")
  @Pattern(regexp = Regex.PASSWORD_REQ, message = "Nowe hasło musi zawierać co najmniej 8 " +
    "znaków, jedną wielką literę, jedną cyfrę i znak specjalny (spośród #?!@$%^&*).")
  private String newPassword;

  @NotBlank(message = "Potwierdzenie nowego hasła jest wymagane.")
  private String confirmedNewPassword;

  @Override
  public List<String> getMatchValues() {
    return List.of(newPassword, confirmedNewPassword);
  }
}
