package pl.polsl.tai.security.resolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Component
public class RenewCookieFilter extends OncePerRequestFilter {

  @Value("${application.session.cookie.name}")
  private String cookieName;

  @Value("${server.servlet.session.timeout}")
  private int cookieMaxAgeSec;

  @Override
  protected void doFilterInternal(
    @NotNull HttpServletRequest req,
    @NotNull HttpServletResponse res,
    @NotNull FilterChain chain
  ) throws ServletException, IOException {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Arrays.stream(req.getCookies())
        .filter(c -> c.getName().equals(cookieName))
        .findFirst()
        .ifPresent(cookie -> {
          final String sessionId = req.getSession().getId();
          final Cookie renewCookie = new Cookie(cookieName, sessionId);
          renewCookie.setHttpOnly(cookie.isHttpOnly());
          renewCookie.setPath("/");
          renewCookie.setSecure(cookie.getSecure());
          renewCookie.setDomain(cookie.getDomain());
          renewCookie.setMaxAge((int) Duration.ofSeconds(cookieMaxAgeSec).getSeconds());
          res.addCookie(renewCookie);
        });
    }
    chain.doFilter(req, res);
  }
}
