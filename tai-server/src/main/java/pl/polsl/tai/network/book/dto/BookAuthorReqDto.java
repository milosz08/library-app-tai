package pl.polsl.tai.network.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookAuthorReqDto {
  @NotBlank(message = "Imię jest wymagane.")
  @Size(min = 2, max = 100, message = "Imię musi mieć od 2 do 100 znaków.")
  private String firstName;

  @NotBlank(message = "Nazwisko jest wymagane.")
  @Size(min = 2, max = 100, message = "Nazwisko musi mieć od 2 do 100 znaków.")
  private String lastName;

  @Override
  public String toString() {
    return "{" +
      "firstName=" + firstName +
      ", lastName=" + lastName +
      '}';
  }
}
