package pl.polsl.tai.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NaiveIsbnGenerator {
  private static final Random RANDOM = new Random();
  private static final String DEFAULT_PREFIX = "978";

  public static String generateRandom() {
    StringBuilder sb = new StringBuilder(DEFAULT_PREFIX);
    for (int i = 0; i < 10; i++) {
      sb.append(RANDOM.nextInt(10));
    }
    final String baseISBN = sb.toString();
    final String raw = baseISBN + calculateCheckDigit(baseISBN);
    final List<String> subSequences = List.of(
      raw.substring(0, 3),
      raw.substring(3, 5),
      raw.substring(5, 11),
      raw.substring(11, 13),
      raw.substring(13)
    );
    return String.join("-", subSequences);
  }

  private static int calculateCheckDigit(String baseISBN) {
    int sum = 0;
    for (int i = 0; i < baseISBN.length(); i++) {
      int digit = Character.getNumericValue(baseISBN.charAt(i));
      sum += (i % 2 == 0) ? digit : digit * 3;
    }
    final int remainder = sum % 10;
    return (remainder == 0) ? 0 : 10 - remainder;
  }
}
