package pl.polsl.tai.network.logs;

import pl.polsl.tai.network.logs.dto.DeletedLogRowsCountResDto;
import pl.polsl.tai.network.logs.dto.LogsContainerResDto;
import pl.polsl.tai.security.LoggedUser;

public interface LogsService {
	LogsContainerResDto getNewestPageableLogs(Integer page, Integer size);

	void deleteLogById(long id, LoggedUser loggedUser);

	DeletedLogRowsCountResDto deleteAllLogs(LoggedUser loggedUser);
}
