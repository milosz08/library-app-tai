package pl.polsl.tai.domain.user;

import jakarta.persistence.*;
import lombok.*;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.role.RoleEntity;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserEntity extends EntityBase implements Serializable {

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	@Column(insertable = false)
	private Boolean active;

	@ManyToOne
	@JoinColumn
	private RoleEntity role;

	@Override
	public String toString() {
		return "{" +
			"firstName=" + firstName +
			", lastName=" + lastName +
			", email=" + email +
			'}';
	}
}
