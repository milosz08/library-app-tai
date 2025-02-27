package pl.polsl.tai.i18n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Configuration
class I18nConfig implements WebMvcConfigurer {

  @Value("${application.locale}")
  private String locale;

  @Bean
  LocaleResolver localeResolver(FixedOneLocaleResolver fixedOneLocaleResolver) {
    fixedOneLocaleResolver.setDefaultLocale(createLocale());
    fixedOneLocaleResolver.setSupportedLocales(List.of(createLocale()));
    return fixedOneLocaleResolver;
  }

  @Primary
  @Bean
  MessageSource messageSource() {
    final ResourceBundleMessageSource messageSource = new SpringSecurityMessageSource();
    messageSource.setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
    messageSource.setDefaultLocale(createLocale());
    return messageSource;
  }

  private Locale createLocale() {
    return new Locale(locale);
  }
}
