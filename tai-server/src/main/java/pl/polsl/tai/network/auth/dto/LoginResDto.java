package pl.polsl.tai.network.auth.dto;

public record LoginResDto(boolean activated, String role, String roleName) {
	public LoginResDto(boolean activated) {
		this(activated, null, null);
	}
}
