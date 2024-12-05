package pl.polsl.tai.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class FirstLoginPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
	public FirstLoginPasswordAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}
}
