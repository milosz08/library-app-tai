package pl.polsl.tai.network.auth;

import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;

import java.util.Optional;

interface AuthService {
	Optional<String> login(LoginReqDto reqDto);

	String register(RegisterReqDto reqDto);

	void activateAccount(String token);
}
