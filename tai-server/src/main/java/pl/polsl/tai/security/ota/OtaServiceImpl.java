package pl.polsl.tai.security.ota;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.util.SecureRandomGenerator;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
class OtaServiceImpl implements OtaService {
	private final OtaTokenRepository otaTokenRepository;
	private final OtaProperties otaProperties;

	@Override
	public GeneratedOta generateToken(OtaType otaType, Duration duration, UserEntity user) {
		final long expiredSeconds = duration.toSeconds();
		String token;
		do {
			token = SecureRandomGenerator.generate(otaProperties.getLength());
		} while (otaTokenRepository.existsByToken(token));

		final OtaTokenEntity otaToken = OtaTokenEntity.builder()
			.token(token)
			.expires(LocalDateTime.now().plusSeconds(expiredSeconds))
			.type(OtaType.ACTIVATE_ACCOUNT)
			.user(user)
			.build();

		log.info("Generated ota token with type: {} and time: {} for user: {}", otaType.name(), expiredSeconds, user);
		return new GeneratedOta(otaToken, expiredSeconds);
	}

	@Override
	public OtaTokenEntity validateToken(OtaType otaType, String token) {
		return otaTokenRepository
			.findAndValidateTokenByType(token, otaType, LocalDateTime.now())
			.orElseThrow(() -> new RestServerException("Podano nieprawidłowy token."));
	}
}
