package pl.polsl.tai.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Void> handleNotFound() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		final Map<String, String> errors = new HashMap<>();
		for (final FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		log.error("Invalid method argument exception. Cause(s): {}", errors);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> handleAuthenticationException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException() {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@ExceptionHandler(RestServerException.class)
	ResponseEntity<ErrorDto> handleRestServerException(RestServerException ex) {
		log.error("Server rest exception. Cause: {}", ex.getMessage());
		return new ResponseEntity<>(new ErrorDto(ex.getMessage()), ex.getStatus());
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorDto> handleUnknownException(Exception ex) {
		log.error("Unknown server exception. Cause: {}", ex.getMessage());
		return new ResponseEntity<>(
			new ErrorDto(RestServerException.UNEXPECTED_EXCEPTION_MESSAGE),
			HttpStatus.INTERNAL_SERVER_ERROR
		);
	}
}
