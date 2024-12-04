package pl.polsl.tai.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.log.Level;
import pl.polsl.tai.domain.log.LogEntity;
import pl.polsl.tai.domain.log.LogRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogPersistServiceImpl implements LogPersistService {
	private final LogRepository logRepository;

	private void log(Level level, String message, Object... args) {
		final String formattedMessage = String.format(message, args);
		final LocalDateTime executedTime = LocalDateTime.now();

		final LogEntity logEntity = new LogEntity(formattedMessage, level, executedTime);
		final LogEntity persistedLog = logRepository.save(logEntity);

		log.debug("Persist log: {} into DB.", persistedLog);
	}

	public void info(String message, Object... args) {
		log(Level.INFO, message, args);
	}

	public void error(String message, Object... args) {
		log(Level.ERROR, message, args);
	}
}
