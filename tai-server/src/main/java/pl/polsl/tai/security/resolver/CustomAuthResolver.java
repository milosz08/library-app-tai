package pl.polsl.tai.security.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthResolver extends ResponseResolverBase implements AuthenticationEntryPoint {
  public CustomAuthResolver(ObjectMapper objectMapper) {
    super(objectMapper);
  }

  @Override
  public void commence(
    HttpServletRequest req,
    HttpServletResponse res,
    AuthenticationException ex
  ) throws IOException {
    log.error("Authentication exception: {}.", ex.getMessage());
    sendResponse(res, ex.getMessage());
  }

  @Override
  protected int status() {
    return HttpServletResponse.SC_UNAUTHORIZED;
  }
}
