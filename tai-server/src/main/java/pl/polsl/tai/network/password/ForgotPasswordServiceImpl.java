package pl.polsl.tai.network.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.mail.MailProperties;
import pl.polsl.tai.mail.MailService;
import pl.polsl.tai.mail.MailTemplate;
import pl.polsl.tai.network.password.dto.ChangePasswordReqDto;
import pl.polsl.tai.network.password.dto.RequestChangePasswordReqDto;
import pl.polsl.tai.security.ota.GeneratedOta;
import pl.polsl.tai.security.ota.OtaProperties;
import pl.polsl.tai.security.ota.OtaService;
import pl.polsl.tai.util.DateTime;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class ForgotPasswordServiceImpl implements ForgotPasswordService {
  private final OtaService otaService;
  private final LogPersistService logPersistService;
  private final OtaProperties otaProperties;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final MailProperties mailProperties;

  private final UserRepository userRepository;
  private final OtaTokenRepository otaTokenRepository;

  @Override
  public void sendRequestToChangePassword(RequestChangePasswordReqDto reqDto) {
    final Optional<UserEntity> optionalUser = userRepository.findByEmail(reqDto.getEmail());

    if (optionalUser.isPresent()) {
      final Duration expiredDuration = Duration.ofMinutes(otaProperties.getShortExpiredMin());
      final GeneratedOta ota = otaService
        .generateToken(OtaType.RESET_PASSWORD, expiredDuration, optionalUser.get());
      otaTokenRepository.save(ota.entity());

      final String token = ota.entity().getToken();
      final UserEntity user = optionalUser.get();

      final String changePasswordLink = String.format("%s/przypomnij-haslo/%s",
        mailProperties.getClientUrl(), token);

      final Context context = new Context();
      context.setVariable("name", user.getFirstName() + " " + user.getLastName());
      context.setVariable("token", token);
      context.setVariable("changePasswordLink", changePasswordLink);
      context.setVariable("tokenExpiration", DateTime.formatSeconds(ota.expiredSeconds()));

      mailService.send(user.getEmail(), "Zmiana hasła", context, MailTemplate.FORGOT_PASSWORD);
      log.info("Sent request to change password for user: {}.", optionalUser.get());
    }
  }

  @Override
  @Transactional
  public void changePassword(String token, ChangePasswordReqDto reqDto) {
    final OtaTokenEntity otaToken = otaService.validateToken(OtaType.RESET_PASSWORD, token);

    final UserEntity user = otaToken.getUser();
    user.setPassword(passwordEncoder.encode(reqDto.getNewPassword()));

    otaToken.setUsed(true);

    log.info("User: {} changed password.", user);
    logPersistService.info("Użytkownik z adresem email: %s zaktualizował swoje hasło.",
      user.getEmail());
  }
}
