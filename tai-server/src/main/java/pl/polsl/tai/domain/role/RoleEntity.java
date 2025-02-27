package pl.polsl.tai.domain.role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.tai.domain.EntityBase;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class RoleEntity extends EntityBase implements Serializable {

  @Enumerated(EnumType.STRING)
  private UserRole name;

  @Override
  public String toString() {
    return "{" +
      "name=" + name.name() +
      '}';
  }
}
