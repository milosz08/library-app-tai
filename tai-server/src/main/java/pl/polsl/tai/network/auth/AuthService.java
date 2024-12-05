package pl.polsl.tai.network.auth;

import pl.polsl.tai.dto.TokenResDto;
import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;

import java.util.Map;

interface AuthService {
	Map<String, Object> login(LoginReqDto reqDto);

	TokenResDto register(RegisterReqDto reqDto);

	void activateAccount(String token);
}
