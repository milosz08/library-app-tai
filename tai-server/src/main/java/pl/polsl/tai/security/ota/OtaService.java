package pl.polsl.tai.security.ota;

import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.user.UserEntity;

import java.time.Duration;

public interface OtaService {
	GeneratedOta generateToken(OtaType otaType, Duration duration, UserEntity user);

	OtaTokenEntity validateToken(OtaType otaType, String token);
}
