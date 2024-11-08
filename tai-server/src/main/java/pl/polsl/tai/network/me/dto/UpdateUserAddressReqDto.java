package pl.polsl.tai.network.me.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserAddressReqDto {
	@NotBlank(message = "Ulica jest wymagana.")
	@Size(min = 2, max = 100, message = "Nazwa ulicy musi mieć od 2 do 100 znaków.")
	private String street;

	@NotBlank(message = "Numer budynku jest wymagany.")
	@Size(max = 10, message = "Numer budynku może mieć maksymalnie do 10 znaków.")
	private String buildingNumber;

	@Size(max = 10, message = "Numer mieszkania może mieć maksymalnie do 10 znaków.")
	private String apartmentNumber;

	@NotBlank(message = "Miasto jest wymagane.")
	@Size(min = 2, max = 100, message = "Miasto musi mieć od 2 do 100 znaków.")
	private String city;
}
