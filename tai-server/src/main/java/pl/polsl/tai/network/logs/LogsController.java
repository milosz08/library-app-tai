package pl.polsl.tai.network.logs;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.network.logs.dto.DeletedLogRowsCountResDto;
import pl.polsl.tai.network.logs.dto.LogsContainerResDto;
import pl.polsl.tai.security.LoggedUser;

@RestController
@RequestMapping("/v1/logs")
@RequiredArgsConstructor
public class LogsController {
	private final LogsService logsService;

	@GetMapping
	ResponseEntity<LogsContainerResDto> getNewestPageableLogs(
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size
	) {
		return ResponseEntity.ok(logsService.getNewestPageableLogs(page, size));
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteLogById(@PathVariable Long id, @AuthenticationPrincipal LoggedUser loggedUser) {
		logsService.deleteLogById(id, loggedUser);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping
	ResponseEntity<DeletedLogRowsCountResDto> deleteAllLogs(@AuthenticationPrincipal LoggedUser loggedUser) {
		return ResponseEntity.ok(logsService.deleteAllLogs(loggedUser));
	}
}
