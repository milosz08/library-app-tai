package pl.polsl.tai.security.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import pl.polsl.tai.exception.ErrorDto;

import java.io.IOException;

@RequiredArgsConstructor
abstract class ResponseResolverBase {
	private final ObjectMapper objectMapper;

	protected void sendResponse(HttpServletResponse response, String message) throws IOException {
		String messageWithDot = message;
		if (!message.endsWith(".")) {
			messageWithDot += ".";
		}
		final String jsonResponse = objectMapper.writeValueAsString(new ErrorDto(messageWithDot));
		response.setStatus(status());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(jsonResponse);
	}

	protected abstract int status();
}
