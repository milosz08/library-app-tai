package pl.polsl.tai.network.rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnBookReqDto {
  @NotNull(message = "Identyfikator książki musi istnieć.")
  private Long bookId;

  @NotNull(message = "Ilość zwracanych książek musi istnieć.")
  @Min(value = 1, message = "Ilość zwracanych książek musi być większa bądź równa 1.")
  private Long count;
}
