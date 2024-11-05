package pl.polsl.tai.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppCorsConfiguration implements CorsConfigurationSource {
	private final CorsProperties corsProperties;

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		final CorsConfiguration config = new CorsConfiguration();
		final List<String> allowedMethods = Arrays.stream(HttpMethod.values())
			.map(HttpMethod::name)
			.toList();

		config.setAllowedOrigins(corsProperties.getUrls());
		config.setAllowedMethods(allowedMethods);
		config.setAllowCredentials(true);
		config.setAllowedHeaders(List.of("*"));

		return config;
	}
}
