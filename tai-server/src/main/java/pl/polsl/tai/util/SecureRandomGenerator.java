package pl.polsl.tai.util;

import java.security.SecureRandom;

public class SecureRandomGenerator {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom secureRandom = new SecureRandom();

	public static String generate(int length) {
		final StringBuilder stringBuilder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = secureRandom.nextInt(CHARACTERS.length());
			stringBuilder.append(CHARACTERS.charAt(randomIndex));
		}
		return stringBuilder.toString();
	}
}
