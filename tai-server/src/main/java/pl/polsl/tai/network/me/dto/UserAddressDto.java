package pl.polsl.tai.network.me.dto;

import pl.polsl.tai.domain.address.AddressEntity;

public record UserAddressDto(String street, String buildingNumber, String apartmentNumber, String city) {
	public UserAddressDto(AddressEntity address) {
		this(address.getStreet(), address.getBuildingNumber(), address.getApartmentNumber(), address.getCity());
	}
}
