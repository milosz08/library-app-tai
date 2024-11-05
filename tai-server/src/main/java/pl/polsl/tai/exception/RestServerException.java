package pl.polsl.tai.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestServerException extends RuntimeException {
	static final String UNEXPECTED_EXCEPTION_MESSAGE = "Wystąpił nieznany błąd serwera.";

	private final HttpStatus status;

	public RestServerException(String message) {
		super(message);
		this.status = HttpStatus.BAD_REQUEST;
	}

	public RestServerException() {
		super(UNEXPECTED_EXCEPTION_MESSAGE);
		status = HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
