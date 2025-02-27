package pl.polsl.tai.network.me.dto;

import pl.polsl.tai.domain.user.UserEntity;

public record UpdatedUserDetailsResDto(String firstName, String lastName) {
  public UpdatedUserDetailsResDto(UserEntity user) {
    this(user.getFirstName(), user.getLastName());
  }
}
