package pl.polsl.tai.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.polsl.tai.domain.role.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByIdAndRole_Name(Long userId, UserRole role);

  List<UserEntity> findAllByIdInAndRole_Name(List<Long> userIds, UserRole role);

  List<UserEntity> findAllByRole_Name(UserRole role);

  boolean existsByEmail(String email);

  @Query("from UserEntity u left join fetch u.books where u.id = :userId")
  Optional<UserEntity> findUserAndFetchBooks(@Param("userId") Long userId);
}
