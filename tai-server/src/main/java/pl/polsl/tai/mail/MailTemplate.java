package pl.polsl.tai.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailTemplate {
	ACTIVATE_ACCOUNT("activate-account"),
	CREATE_EMPLOYER_ACCOUNT("create-employer-account"),
	FORGOT_PASSWORD("forgot-password"),
	REGENERATE_TOKEN_ACTIVATE_EMPLOYER("regenerate-token-activate-employer"),
	REGISTER_ACCOUNT("register-account"),
	;

	private final String templateName;
}
