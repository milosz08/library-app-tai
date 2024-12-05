package pl.polsl.tai.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.tai.domain.role.UserRole;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByIdAndRole_Name(Long id, UserRole role);

	Page<UserEntity> findAllByRole_Name(Pageable pageable, UserRole role);

	boolean existsByEmail(String email);
}
