package pl.polsl.tai.network.auth.dto;

public record RevalidateSessionResDto(boolean authenticated, String roleName, String role) {
}
