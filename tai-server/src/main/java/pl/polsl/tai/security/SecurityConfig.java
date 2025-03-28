package pl.polsl.tai.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.polsl.tai.domain.role.UserRole;
import pl.polsl.tai.i18n.FixedOneLocaleFilter;
import pl.polsl.tai.security.resolver.CustomAccessDeniedResolver;
import pl.polsl.tai.security.resolver.CustomAuthResolver;
import pl.polsl.tai.security.resolver.CustomLogoutHandlerResolver;
import pl.polsl.tai.security.resolver.RenewCookieFilter;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;
import static pl.polsl.tai.domain.role.UserRole.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {
  private final CustomAuthResolver authResolver;
  private final CustomAccessDeniedResolver accessDeniedResolver;
  private final FixedOneLocaleFilter fixedOneLocaleFilter;
  private final RenewCookieFilter renewCookieFilter;
  private final CustomLogoutHandlerResolver logoutHandlerResolver;

  @Value("${application.session.cookie.name}")
  private String sessionCookieName;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .addFilterBefore(fixedOneLocaleFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(renewCookieFilter, UsernamePasswordAuthenticationFilter.class)
      .csrf(Customizer.withDefaults())
      .sessionManagement(config -> {
        config.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        config.sessionFixation().migrateSession();
      })
      .exceptionHandling(config -> {
        config.authenticationEntryPoint(authResolver);
        config.accessDeniedHandler(accessDeniedResolver);
      })
      .authorizeHttpRequests(auth -> {
        // CsrfController
        auth.requestMatchers(GET, "/api/v1/csrf/token").permitAll();
        // AuthController
        auth.requestMatchers(POST, "/api/v1/auth/login").permitAll();
        auth.requestMatchers(POST, "/api/v1/auth/register").permitAll();
        auth.requestMatchers(PATCH, "/api/v1/auth/activate/**").permitAll();
        auth.requestMatchers(PATCH, "/api/v1/auth/session/revalidate").permitAll();
        // ForgotPasswordController
        auth.requestMatchers(PATCH, "/api/v1/forgot/password/**").permitAll();
        // MeController
        auth.requestMatchers(PATCH, "/api/v1/@me").hasAnyRole(mapRoles(CUSTOMER, ADMIN));
        auth.requestMatchers(PATCH, "/api/v1/@me/address").hasAnyRole(mapRoles(CUSTOMER));
        auth.requestMatchers(DELETE, "/api/v1/@me").hasAnyRole(mapRoles(CUSTOMER));
        // LogController
        auth.requestMatchers(GET, "/api/v1/logs").hasAnyRole(mapRoles(ADMIN));
        auth.requestMatchers(DELETE, "/api/v1/logs/**").hasAnyRole(mapRoles(ADMIN));
        auth.requestMatchers(DELETE, "/api/v1/logs").hasAnyRole(mapRoles(ADMIN));
        // EmployerController
        auth.requestMatchers(GET, "/api/v1/employer").hasAnyRole(mapRoles(ADMIN));
        auth.requestMatchers(POST, "/api/v1/employer").hasAnyRole(mapRoles(ADMIN));
        auth.requestMatchers(PATCH, "/api/v1/employer/first/access/**").permitAll();
        auth.requestMatchers(PATCH, "/api/v1/employer/**").hasAnyRole(mapRoles(ADMIN));
        auth.requestMatchers(DELETE, "/api/v1/employer/**").hasAnyRole(mapRoles(ADMIN));
        auth.requestMatchers(DELETE, "/api/v1/employer").hasAnyRole(mapRoles(ADMIN));
        // BookController
        auth.requestMatchers(GET, "/api/v1/book/**").hasAnyRole(mapRoles(CUSTOMER, EMPLOYER));
        auth.requestMatchers(POST, "/api/v1/book").hasAnyRole(mapRoles(EMPLOYER));
        auth.requestMatchers(PATCH, "/api/v1/book/**").hasAnyRole(mapRoles(EMPLOYER));
        auth.requestMatchers(DELETE, "/api/v1/book/**").hasAnyRole(mapRoles(EMPLOYER));
        auth.requestMatchers(DELETE, "/api/v1/book").hasAnyRole(mapRoles(EMPLOYER));
        // RentalController
        auth.requestMatchers(GET, "/api/v1/rental/rented/**").hasAnyRole(mapRoles(CUSTOMER));
        auth.requestMatchers(PATCH, "/api/v1/rental/loan").hasAnyRole(mapRoles(CUSTOMER));
        auth.requestMatchers(DELETE, "/api/v1/rental/return").hasAnyRole(mapRoles(CUSTOMER));
        // other stuff
        auth.requestMatchers(GET, "/actuator/**").permitAll();
        auth.requestMatchers(OPTIONS, "/**").permitAll();
        // any other api requests are authenticated
        auth.requestMatchers("/api/**").authenticated();
        // front end SPA files
        auth.requestMatchers("/**").permitAll();
      })
      .logout(config -> {
        config.logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout",
          HttpMethod.DELETE.name()));
        config.addLogoutHandler(new HeaderWriterLogoutHandler(
          new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES))
        );
        config.deleteCookies(sessionCookieName);
        config.logoutSuccessHandler(logoutHandlerResolver);
      })
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable);
    return http.build();
  }

  private String[] mapRoles(UserRole... roles) {
    return Arrays.stream(roles).map(UserRole::name).toArray(String[]::new);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  AuthenticationManager authenticationManager(
    MessageSource messageSource,
    UserDetailsService userDetailsService
  ) {
    final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setMessageSource(messageSource);
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    return new ProviderManager(provider);
  }

  @Bean
  AuthenticationManager loginPageAuthenticationManager(
    MessageSource messageSource,
    UserDetailsService userDetailsService
  ) {
    final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setMessageSource(messageSource);
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    provider.setPreAuthenticationChecks(new LoginAuthenticationCheck());
    provider.setPostAuthenticationChecks(new LoginAuthenticationCheck());
    return new ProviderManager(provider);
  }

  @Bean
  SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }
}
