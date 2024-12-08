package pl.polsl.tai.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Void> handleNotFound() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		if (fieldErrors.isEmpty()) {
			final ObjectError error = ex.getBindingResult().getAllErrors().get(0);
			log.error("Invalid method argument exception. Cause: {}", error);
			return ResponseEntity.badRequest().body(new ErrorDto(error.getDefaultMessage()));
		}
		final Map<String, String> errors = new HashMap<>();
		for (final FieldError fieldError : fieldErrors) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		log.error("Invalid method argument exception. Cause(s): {}", errors);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(AuthenticationException.class)
	ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) throws AuthenticationException {
		throw ex;
	}

	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) throws AccessDeniedException {
		throw ex;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	ResponseEntity<Void> handleHttpRequestMethodNotSupported() {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
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
