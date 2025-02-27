package pl.polsl.tai.security;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import pl.polsl.tai.domain.role.UserRole;

class LoginAuthenticationCheck implements UserDetailsChecker {
  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  @Override
  public void check(UserDetails userDetails) {
    // throw disabled account exception only for admin and employer
    if (
      !userDetails.getAuthorities().contains(UserRole.CUSTOMER.toAuthority()) &&
        !userDetails.isEnabled()
    ) {
      throw new DisabledException(messages
        .getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
    }
  }
}
