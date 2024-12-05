package pl.polsl.tai.network.logs;

import pl.polsl.tai.domain.log.LogEntity;
import pl.polsl.tai.dto.PageableContainerResDto;
import pl.polsl.tai.network.logs.dto.DeletedLogRowsCountResDto;
import pl.polsl.tai.network.logs.dto.LogRowResDto;
import pl.polsl.tai.security.LoggedUser;

public interface LogsService {
	PageableContainerResDto<LogRowResDto, LogEntity> getNewestPageableLogs(Integer page, Integer size);

	void deleteLogById(Long id, LoggedUser loggedUser);

	DeletedLogRowsCountResDto deleteLogsChunk(Integer chunkSize, LoggedUser loggedUser);

	DeletedLogRowsCountResDto deleteAllLogs(LoggedUser loggedUser);
}
