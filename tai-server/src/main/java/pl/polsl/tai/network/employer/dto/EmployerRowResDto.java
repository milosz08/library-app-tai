package pl.polsl.tai.network.employer.dto;

import pl.polsl.tai.domain.user.UserEntity;

public record EmployerRowResDto(
  long id,
  String firstName,
  String lastName,
  String email,
  boolean active
) {
  public EmployerRowResDto(UserEntity user) {
    this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getActive());
  }
}
