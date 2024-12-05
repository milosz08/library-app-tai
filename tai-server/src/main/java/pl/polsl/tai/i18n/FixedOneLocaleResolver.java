package pl.polsl.tai.i18n;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Component
class FixedOneLocaleResolver extends AcceptHeaderLocaleResolver {

	@Value("${application.locale}")
	private String locale;

	@Override
	public @NotNull Locale resolveLocale(@NotNull HttpServletRequest req) {
		final Locale localeObj = createLocale();
		LocaleContextHolder.setLocale(localeObj);
		return localeObj;
	}

	private Locale createLocale() {
		return Locale.forLanguageTag(locale);
	}
}
