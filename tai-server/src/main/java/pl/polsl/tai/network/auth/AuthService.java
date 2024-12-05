package pl.polsl.tai.network.auth;

import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.network.auth.dto.TokenResDto;

import java.util.Optional;

interface AuthService {
	Optional<TokenResDto> login(LoginReqDto reqDto);

	TokenResDto register(RegisterReqDto reqDto);

	void activateAccount(String token);
}
