package pl.polsl.tai.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.tai.domain.ota.OtaTokenRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
class AppTaskScheduler {
	private final OtaTokenRepository otaTokenRepository;

	// co 1h
	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	void deleteUnusedOtaTokens() {
		otaTokenRepository.deleteAllByExpiresBeforeAndUsedIsFalse(LocalDateTime.now());
	}
}
