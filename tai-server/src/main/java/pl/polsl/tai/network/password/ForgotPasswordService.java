package pl.polsl.tai.network.password;

import pl.polsl.tai.network.password.dto.ChangePasswordReqDto;
import pl.polsl.tai.network.password.dto.RequestChangePasswordReqDto;

interface ForgotPasswordService {
	void sendRequestToChangePassword(RequestChangePasswordReqDto reqDto);

	void changePassword(String token, ChangePasswordReqDto reqDto);
}
