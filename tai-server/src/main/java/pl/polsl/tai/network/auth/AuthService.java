package pl.polsl.tai.network.auth;

import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.LoginResDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.network.auth.dto.RevalidateSessionResDto;
import pl.polsl.tai.security.LoggedUser;

interface AuthService {
	LoginResDto login(LoginReqDto reqDto);

	void register(RegisterReqDto reqDto);

	void activateAccount(String token);

	RevalidateSessionResDto revalidateSession(LoggedUser loggedUser);
}
