package pl.polsl.tai.network.logs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.tai.domain.log.LogEntity;
import pl.polsl.tai.domain.log.LogRepository;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.exception.NotFoundRestServerException;
import pl.polsl.tai.network.logs.dto.DeletedLogRowsCountResDto;
import pl.polsl.tai.network.logs.dto.LogRowResDto;
import pl.polsl.tai.security.LoggedUser;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class LogsServiceImpl implements LogsService {
	private final LogRepository logRepository;

	@Override
	public PageableResDto<LogRowResDto, LogEntity> getNewestPageableLogs(Integer page, Integer size) {
		final int pageSafe = page == null ? 1 : page;
		final int sizeSafe = size == null ? 10 : size;

		final Page<LogEntity> pageable = logRepository
			.findAll(PageRequest.of(pageSafe - 1, sizeSafe, Sort.by(Sort.Direction.DESC, "executedTime")));

		final List<LogRowResDto> results = pageable.map(LogRowResDto::new).toList();
		return new PageableResDto<>(results, pageable);
	}

	@Override
	public void deleteLogById(Long logId, LoggedUser loggedUser) {
		if (!logRepository.existsById(logId)) {
			throw new NotFoundRestServerException("Wybrany zapis nie istnieje.");
		}
		logRepository.deleteById(logId);
		log.info("Delete log with id: {} by: {}.", logId, loggedUser.getUsername());
	}

	@Override
	public DeletedLogRowsCountResDto deleteLogsChunk(Integer chunkSize, LoggedUser loggedUser) {
		final int chunkSizeSave = chunkSize == null ? 1 : chunkSize;

		final List<LogEntity> logsFromEnd = logRepository
			.findAllBy(PageRequest.of(0, chunkSizeSave, Sort.by(Sort.Direction.ASC, "id")));

		logRepository.deleteAll(logsFromEnd);
		log.info("Delete logs chunk (size: {}) by: {}.", chunkSize, loggedUser.getUsername());
		return new DeletedLogRowsCountResDto(chunkSizeSave);
	}

	@Override
	@Transactional
	public DeletedLogRowsCountResDto deleteAllLogs(LoggedUser loggedUser) {
		final long affectedRows = logRepository.count();
		logRepository.deleteAll();
		logRepository.resetSequence(0);
		log.info("Delete all logs. Flushed: {} log rows by: {}.", affectedRows, loggedUser.getUsername());
		return new DeletedLogRowsCountResDto(affectedRows);
	}
}
