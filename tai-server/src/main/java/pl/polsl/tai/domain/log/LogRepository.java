package pl.polsl.tai.domain.log;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
	boolean existsById(@NotNull Long id);

	@Modifying
	void deleteById(@NotNull Long id);
}
