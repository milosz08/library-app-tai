package pl.polsl.tai.network.auth;

import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.user.UserEntity;

interface OtaService {
	OtaTokenEntity generateToken(OtaType otaType, UserEntity user);

	OtaTokenEntity validateToken(OtaType otaType, String token);
}
