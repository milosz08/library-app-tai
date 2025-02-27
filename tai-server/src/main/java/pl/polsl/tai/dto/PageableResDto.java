package pl.polsl.tai.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageableResDto<T, U>(
  List<T> rows,
  long totalResults,
  int totalPages,
  int page,
  int perPage
) {
  public PageableResDto(List<T> rows, Page<U> page) {
    this(rows, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
  }
}
