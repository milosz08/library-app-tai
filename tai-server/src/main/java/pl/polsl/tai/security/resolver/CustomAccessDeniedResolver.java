package pl.polsl.tai.security.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedResolver extends ResponseResolverBase implements AccessDeniedHandler {
	public CustomAccessDeniedResolver(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@Override
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException
	) throws IOException {
		log.error(accessDeniedException.getMessage());
		sendResponse(response, "Nieautoryzowany dostęp do zasobu.");
	}

	@Override
	protected int status() {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
