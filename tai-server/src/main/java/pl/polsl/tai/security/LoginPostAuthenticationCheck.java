package pl.polsl.tai.security;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import pl.polsl.tai.domain.role.UserRole;

class LoginPostAuthenticationCheck implements UserDetailsChecker {
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Override
	public void check(UserDetails userDetails) {
		if (userDetails.getAuthorities().contains(UserRole.EMPLOYER.toAuthority()) && !userDetails.isEnabled()) {
			throw new DisabledException(messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
		}
	}
}
