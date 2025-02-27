package pl.polsl.tai.domain.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  CUSTOMER("klient"),
  EMPLOYER("pracownik"),
  ADMIN("administrator"),
  ;

  private final String localeName;

  public GrantedAuthority toAuthority() {
    return new SimpleGrantedAuthority(name());
  }
}
