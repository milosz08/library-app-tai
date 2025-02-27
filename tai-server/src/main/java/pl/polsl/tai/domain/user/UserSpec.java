package pl.polsl.tai.domain.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import pl.polsl.tai.domain.role.UserRole;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpec {
  public static Specification<UserEntity> hasEmail(String email) {
    return (root, query, criteriaBuilder) -> criteriaBuilder
      .like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
  }

  public static Specification<UserEntity> hasRole(UserRole userRole) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role").get("name"), userRole);
  }
}
