package pl.polsl.tai.security.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pl.polsl.tai.log.LogPersistService;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedResolver extends ResponseResolverBase implements AccessDeniedHandler {
	private final LogPersistService logPersistService;

	public CustomAccessDeniedResolver(ObjectMapper objectMapper, LogPersistService logPersistService) {
		super(objectMapper);
		this.logPersistService = logPersistService;
	}

	@Override
	public void handle(
		HttpServletRequest req,
		HttpServletResponse res,
		AccessDeniedException ex
	) throws IOException {
		final String path = req.getServletPath();
		log.error("Exception at: {} invoked. Cause: {}", path, ex.getMessage());
		logPersistService.error("Wystąpiła próba nieuprawnionego dostęp do chronionego zasobu: %s.", path);
		sendResponse(res, "Nieautoryzowany dostęp do zasobu.");
	}

	@Override
	protected int status() {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
