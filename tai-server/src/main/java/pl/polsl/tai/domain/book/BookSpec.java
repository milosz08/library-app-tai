package pl.polsl.tai.domain.book;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookSpec {
	public static Specification<BookEntity> hasTitle(String title) {
		return (root, query, criteriaBuilder) -> criteriaBuilder
			.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
	}

	public static Specification<BookEntity> hasIds(Set<Long> ids) {
		return (root, query, criteriaBuilder) -> {
			final CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.get("id"));
			for (Long id : ids) {
				inClause.value(id);
			}
			return inClause;
		};
	}
}
