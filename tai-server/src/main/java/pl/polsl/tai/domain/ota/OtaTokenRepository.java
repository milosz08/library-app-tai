package pl.polsl.tai.domain.ota;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtaTokenRepository extends JpaRepository<OtaTokenEntity, Long> {
  boolean existsByToken(String token);

  @Query(value = """
    from OtaTokenEntity e join fetch e.user u
    where e.token = :token and e.expires > :now and e.used = false and e.type = :type
    """)
  Optional<OtaTokenEntity> findAndValidateTokenByType(
    @Param("token") String token,
    @Param("type") OtaType type,
    @Param("now") LocalDateTime now
  );

  @Modifying
  @Query("delete from OtaTokenEntity e where e.expires < :now and e.used = false")
  void deleteAllByExpiresBeforeAndUsedIsFalse(@Param("now") LocalDateTime now);
}
