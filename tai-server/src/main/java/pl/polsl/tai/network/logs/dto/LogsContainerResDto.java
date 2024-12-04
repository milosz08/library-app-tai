package pl.polsl.tai.network.logs.dto;

import java.util.List;

public record LogsContainerResDto(
	List<LogRowResDto> rows,
	long totalResults,
	int totalPages,
	int page,
	int perPage
) {
}
