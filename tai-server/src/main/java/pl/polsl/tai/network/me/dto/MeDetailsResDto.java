package pl.polsl.tai.network.me.dto;

import pl.polsl.tai.domain.user.UserEntity;

public record MeDetailsResDto(String firstName, String username, String email, String role) {
	public MeDetailsResDto(UserEntity user) {
		this(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().getName().name());
	}
}
