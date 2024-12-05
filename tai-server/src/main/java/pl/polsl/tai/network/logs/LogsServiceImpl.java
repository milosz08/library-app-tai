package pl.polsl.tai.network.logs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.log.LogEntity;
import pl.polsl.tai.domain.log.LogRepository;
import pl.polsl.tai.dto.PageableContainerResDto;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.network.logs.dto.DeletedLogRowsCountResDto;
import pl.polsl.tai.network.logs.dto.LogRowResDto;
import pl.polsl.tai.security.LoggedUser;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogsServiceImpl implements LogsService {
	private final LogRepository logRepository;

	@Override
	public PageableContainerResDto<LogRowResDto, LogEntity> getNewestPageableLogs(Integer page, Integer size) {
		final int pageSafe = page == null ? 1 : page;
		final int sizeSafe = size == null ? 10 : size;

		final Page<LogEntity> pageable = logRepository
			.findAll(PageRequest.of(pageSafe - 1, sizeSafe, Sort.by(Sort.Direction.DESC, "executedTime")));

		final List<LogRowResDto> results = pageable.map(LogRowResDto::new).toList();
		return new PageableContainerResDto<>(results, pageable);
	}

	@Override
	public void deleteLogById(long id, LoggedUser loggedUser) {
		if (!logRepository.existsById(id)) {
			throw new RestServerException("Wybrany zapis nie istnieje.");
		}
		logRepository.deleteById(id);
		log.info("Delete log with id: {} by: {}.", id, loggedUser.getUsername());
	}

	@Override
	public DeletedLogRowsCountResDto deleteAllLogs(LoggedUser loggedUser) {
		final long affectedRows = logRepository.count();
		logRepository.deleteAll();
		log.info("Delete all logs. Flushed: {} log rows by: {}.", affectedRows, loggedUser.getUsername());
		return new DeletedLogRowsCountResDto(affectedRows);
	}
}
