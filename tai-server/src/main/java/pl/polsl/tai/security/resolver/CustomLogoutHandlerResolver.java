package pl.polsl.tai.security.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import pl.polsl.tai.log.LogPersistService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandlerResolver implements LogoutSuccessHandler {
  private final LogPersistService logPersistService;

  @Override
  public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
    if (auth != null) {
      log.info("User: {} was logged out.", auth.getName());
      logPersistService.info("Użytkownik: %s wylogował się z serwisu.", auth.getName());
    }
    res.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
