package pl.polsl.tai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApplicationBeans {
	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
