package pl.polsl.tai.network.logs.dto;

import pl.polsl.tai.domain.log.Level;
import pl.polsl.tai.domain.log.LogEntity;
import pl.polsl.tai.util.DateTime;

public record LogRowResDto(Long id, Level level, String executedAt, String message) {
	public LogRowResDto(LogEntity logEntity) {
		this(logEntity.getId(), logEntity.getLevel(),
			logEntity.getExecutedTime().format(DateTime.DTF), logEntity.getMessage());
	}
}
