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
import pl.polsl.tai.config.AppCorsConfiguration;
import pl.polsl.tai.i18n.FixedOneLocaleFilter;
import pl.polsl.tai.security.resolver.CustomAccessDeniedResolver;
import pl.polsl.tai.security.resolver.CustomAuthResolver;
import pl.polsl.tai.security.resolver.CustomLogoutHandlerResolver;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {
	private final CustomAuthResolver authResolver;
	private final CustomAccessDeniedResolver accessDeniedResolver;
	private final FixedOneLocaleFilter fixedOneLocaleFilter;
	private final AppCorsConfiguration appCorsConfiguration;

	@Value("${application.session.cookie.name}")
	private String sessionCookieName;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.addFilterBefore(fixedOneLocaleFilter, UsernamePasswordAuthenticationFilter.class)
			.csrf(Customizer.withDefaults())
			.cors(config -> config.configurationSource(appCorsConfiguration))
			.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
			.exceptionHandling(config -> {
				config.authenticationEntryPoint(authResolver);
				config.accessDeniedHandler(accessDeniedResolver);
			})
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll();
				auth.requestMatchers(HttpMethod.GET, "/api/v1/csrf/token").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll();
				auth.requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll();
				auth.requestMatchers(HttpMethod.PATCH, "/api/v1/auth/activate/**").permitAll();
				auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
				auth.anyRequest().authenticated();
			})
			.logout(config -> {
				config.logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout", HttpMethod.DELETE.name()));
				config.addLogoutHandler(new HeaderWriterLogoutHandler(
					new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES))
				);
				config.deleteCookies(sessionCookieName);
				config.logoutSuccessHandler(new CustomLogoutHandlerResolver());
			})
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable);
		return http.build();
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
	AuthenticationManager authenticationManager(MessageSource messageSource, UserDetailsService userDetailsService) {
		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setMessageSource(messageSource);
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return new ProviderManager(provider);
	}

	@Bean
	SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}

	@Bean
	SecureRandom secureRandom() {
		return new SecureRandom();
	}
}
