package pl.polsl.tai.network.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReqDto {
  @NotNull(message = "Pole adresu email musi istnieć.")
  private String email;

  @NotNull(message = "Pole hasła musi istnieć.")
  private String password;
}
