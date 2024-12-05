package pl.polsl.tai.network.employer.dto;

import pl.polsl.tai.domain.user.UserEntity;

public record UpdateEmployerResDto(String firstName, String lastName) {
	public UpdateEmployerResDto(UserEntity user) {
		this(user.getFirstName(), user.getLastName());
	}
}
