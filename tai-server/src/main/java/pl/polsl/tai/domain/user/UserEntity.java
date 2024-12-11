package pl.polsl.tai.domain.user;

import jakarta.persistence.*;
import lombok.*;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.address.AddressEntity;
import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.domain.role.RoleEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private AddressEntity address;

	@ManyToMany(mappedBy = "users")
	private List<BookEntity> books = new ArrayList<>();

	public void attachAddress(AddressEntity address) {
		this.address = address;
		address.setUser(this);
	}

	@Override
	public String toString() {
		return "{" +
			"firstName=" + firstName +
			", lastName=" + lastName +
			", email=" + email +
			'}';
	}
}
