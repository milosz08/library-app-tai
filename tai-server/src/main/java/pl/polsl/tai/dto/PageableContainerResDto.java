package pl.polsl.tai.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageableContainerResDto<T, U>(
	List<T> rows,
	long totalResults,
	int totalPages,
	int page,
	int perPage
) {
	public PageableContainerResDto(List<T> rows, Page<U> page) {
		this(rows, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
	}
}
