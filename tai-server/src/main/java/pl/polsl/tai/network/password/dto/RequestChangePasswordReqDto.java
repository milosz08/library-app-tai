package pl.polsl.tai.network.password.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChangePasswordReqDto {
  @NotBlank(message = "Adres email jest wymagany.")
  @Email(message = "Adres email jest niepoprawny.")
  private String email;
}
