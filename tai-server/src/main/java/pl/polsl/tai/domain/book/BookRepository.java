package pl.polsl.tai.domain.book;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {
	boolean existsByTitle(String title);

	@Query("from BookEntity b left join b.users u where u.id is null")
	List<BookEntity> findAllNotRented();

	@Query("from BookEntity b left join b.users u where u.id is null and b.id in :bookIds")
	List<BookEntity> findAllNotRentedAndHasIds(@Param("bookIds") List<Long> bookIds);

	@Query("select count(*) from BookEntity b join b.users u where u.id = :userId")
	long countAllRentedByUserId(@Param("userId") Long userId);

	@Query("select b.id, count(*) from BookEntity b join b.users group by b.id")
	List<Tuple> findAllRented();

	@Query("select b.id, count(*) from BookEntity b join b.users u where u.id = :userId group by b.id")
	List<Tuple> findAllRentedForUser(@Param("userId") Long userId);

	@Query("select count(*), b from BookEntity b join b.users u where u.id = :userId and b.id = :bookId")
	Optional<Tuple> findRentedByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
