package pl.polsl.tai.domain.role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
	CUSTOMER,
	EMPLOYER,
	ADMIN,
	;

	public GrantedAuthority toAuthority() {
		return new SimpleGrantedAuthority(name());
	}
}
