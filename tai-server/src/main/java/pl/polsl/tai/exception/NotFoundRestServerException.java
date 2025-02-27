package pl.polsl.tai.exception;

import org.springframework.http.HttpStatus;

public class NotFoundRestServerException extends RestServerException {
  public NotFoundRestServerException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
