package pl.polsl.tai.i18n;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
public class FixedOneLocaleFilter extends OncePerRequestFilter {

  @Value("${application.locale}")
  private String locale;

  @Override
  protected void doFilterInternal(
    @NotNull HttpServletRequest req,
    @NotNull HttpServletResponse res,
    FilterChain filterChain
  ) throws ServletException, IOException {
    LocaleContextHolder.setLocale(Locale.forLanguageTag(locale));
    filterChain.doFilter(req, res);
  }
}
