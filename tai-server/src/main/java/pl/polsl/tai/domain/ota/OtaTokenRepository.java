package pl.polsl.tai.domain.ota;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtaTokenRepository extends JpaRepository<OtaTokenEntity, Long> {
	boolean existsByToken(String token);

	@Query(value = """
		from OtaTokenEntity e join fetch e.user u
		where e.token = :token and e.expires > current_timestamp() and e.used = false and e.type = :type
		""")
	Optional<OtaTokenEntity> findAndValidateTokenByType(@Param("token") String token, @Param("type") OtaType type);
}
