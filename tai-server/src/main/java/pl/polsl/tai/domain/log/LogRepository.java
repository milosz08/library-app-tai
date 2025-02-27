package pl.polsl.tai.domain.log;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
  List<LogEntity> findAllBy(Pageable pageable);

  boolean existsById(@NotNull Long logId);

  @Modifying
  void deleteById(@NotNull Long logId);

  @Modifying
  @Query(value = "ALTER TABLE logs AUTO_INCREMENT = :value", nativeQuery = true)
  void resetSequence(int value);
}
