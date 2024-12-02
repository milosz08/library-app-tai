package pl.polsl.tai.network.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.exception.RestServerException;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
class OtaServiceImpl implements OtaService {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	private final SecureRandom secureRandom;

	private final OtaTokenRepository otaTokenRepository;

	@Value("${application.ota.length}")
	private int otaLength;

	@Value("${application.ota.expired-min}")
	private int otaExpiredMin;

	@Override
	public OtaTokenEntity generateToken(OtaType otaType, UserEntity user) {
		String token;
		do {
			final StringBuilder stringBuilder = new StringBuilder(otaLength);
			for (int i = 0; i < otaLength; i++) {
				int randomIndex = secureRandom.nextInt(CHARACTERS.length());
				stringBuilder.append(CHARACTERS.charAt(randomIndex));
			}
			token = stringBuilder.toString();
		} while (otaTokenRepository.existsByToken(token));

		final OtaTokenEntity otaToken = OtaTokenEntity.builder()
			.token(token)
			.expires(LocalDateTime.now().plusMinutes(otaExpiredMin))
			.type(OtaType.ACTIVATE_ACCOUNT)
			.user(user)
			.build();

		log.info("Generated ota token with type: {} for user: {}", otaType.name(), user);
		return otaToken;
	}

	@Override
	public OtaTokenEntity validateToken(OtaType otaType, String token) {
		return otaTokenRepository
			.findAndValidateTokenByType(token, otaType, LocalDateTime.now())
			.orElseThrow(() -> new RestServerException("Podano nieprawidłowy token."));
	}
}
