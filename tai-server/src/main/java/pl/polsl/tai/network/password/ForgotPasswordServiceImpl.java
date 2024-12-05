package pl.polsl.tai.network.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.dto.TokenResDto;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.network.password.dto.ChangePasswordReqDto;
import pl.polsl.tai.network.password.dto.RequestChangePasswordReqDto;
import pl.polsl.tai.security.ota.GeneratedOta;
import pl.polsl.tai.security.ota.OtaProperties;
import pl.polsl.tai.security.ota.OtaService;
import pl.polsl.tai.util.SecureRandomGenerator;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
	private final OtaService otaService;
	private final LogPersistService logPersistService;
	private final OtaProperties otaProperties;
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	private final OtaTokenRepository otaTokenRepository;

	@Override
	public TokenResDto sendRequestToChangePassword(RequestChangePasswordReqDto reqDto) {
		final Optional<UserEntity> optionalUser = userRepository.findByEmail(reqDto.getEmail());

		String token = SecureRandomGenerator.generate(otaProperties.getLength());
		Duration expiredSeconds = Duration.ofMinutes(otaProperties.getShortExpiredMin());

		if (optionalUser.isPresent()) {
			final GeneratedOta ota = otaService.generateToken(OtaType.RESET_PASSWORD, expiredSeconds, optionalUser.get());
			otaTokenRepository.save(ota.entity());

			token = ota.entity().getToken();
			expiredSeconds = Duration.ofSeconds(ota.expiredSeconds());
			log.info("Send request to change password for user: {}.", optionalUser.get());
		}
		// This OTA token should be sent via email sender but... I suppose we don't have to.
		return new TokenResDto(token, expiredSeconds.toSeconds());
	}

	@Override
	@Transactional
	public void changePassword(String token, ChangePasswordReqDto reqDto) {
		final OtaTokenEntity otaToken = otaService.validateToken(OtaType.RESET_PASSWORD, token);

		final UserEntity user = otaToken.getUser();
		user.setPassword(passwordEncoder.encode(reqDto.getNewPassword()));

		otaToken.setUsed(true);

		log.info("User: {} changed password.", user);
		logPersistService.info("Użytkownik z adresem email: %s zaktualizował swoje hasło.", user.getEmail());
	}
}
