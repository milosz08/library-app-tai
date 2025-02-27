package pl.polsl.tai.network.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import pl.polsl.tai.domain.address.AddressEntity;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.role.RoleEntity;
import pl.polsl.tai.domain.role.RoleRepository;
import pl.polsl.tai.domain.role.UserRole;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.mail.MailProperties;
import pl.polsl.tai.mail.MailService;
import pl.polsl.tai.mail.MailTemplate;
import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.LoginResDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.network.auth.dto.RevalidateSessionResDto;
import pl.polsl.tai.security.LoggedUser;
import pl.polsl.tai.security.ota.GeneratedOta;
import pl.polsl.tai.security.ota.OtaProperties;
import pl.polsl.tai.security.ota.OtaService;
import pl.polsl.tai.util.DateTime;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {
  private final AuthenticationManager loginPageAuthenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final OtaService otaService;
  private final LogPersistService logPersistService;
  private final OtaProperties otaProperties;
  private final MailService mailService;
  private final MailProperties mailProperties;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final OtaTokenRepository otaTokenRepository;

  @Override
  public LoginResDto login(LoginReqDto reqDto) {
    final var authInputToken = UsernamePasswordAuthenticationToken.unauthenticated(
      reqDto.getEmail(),
      reqDto.getPassword()
    );
    final Authentication authentication = loginPageAuthenticationManager
      .authenticate(authInputToken);
    final UserEntity user = ((LoggedUser) authentication.getPrincipal()).userEntity();
    if (authentication.isAuthenticated() && user.getActive()) {
      final SecurityContext context = SecurityContextHolder.getContext();
      context.setAuthentication(authentication);

      log.info("User: {} was logged in.", user);
      logPersistService.info("Użytkownik: %s zalogował się do serwisu.", user.getEmail());

      return new LoginResDto(true, user.getRole().getName().name(),
        user.getRole().getName().getLocaleName());
    }
    final GeneratedOta ota = generateActivateAccountOta(user);
    otaTokenRepository.save(ota.entity());

    sendActivateAccountEmail(ota, user, "Aktywacja konta", MailTemplate.ACTIVATE_ACCOUNT);

    log.info("User: {} was tried to log in, but his account is inactive.", user.getEmail());
    logPersistService.info(
      "Użytkownik: %s próbował zalogować się do serwisu, ale jego konto jest nieaktywne.",
      user.getEmail()
    );
    return new LoginResDto(false);
  }

  @Override
  @Transactional
  public void register(RegisterReqDto reqDto) {
    final RoleEntity role = roleRepository
      .findByName(UserRole.CUSTOMER)
      .orElseThrow(RestServerException::new);

    final UserEntity user = UserEntity.builder()
      .firstName(reqDto.getFirstName())
      .lastName(reqDto.getLastName())
      .email(reqDto.getEmail())
      .password(passwordEncoder.encode(reqDto.getPassword()))
      .role(role)
      .build();

    final AddressEntity address = AddressEntity.builder()
      .street(reqDto.getStreet())
      .buildingNumber(reqDto.getBuildingNumber())
      .apartmentNumber(reqDto.getApartmentNumber())
      .city(reqDto.getCity())
      .user(user)
      .build();

    final GeneratedOta ota = generateActivateAccountOta(user);

    sendActivateAccountEmail(ota, user, "Utworzono konto", MailTemplate.REGISTER_ACCOUNT);

    user.attachAddress(address);
    userRepository.save(user);
    otaTokenRepository.save(ota.entity());

    log.info("Created new customer account: {}.", user);
    logPersistService.info("Utworzono nowe konto klienckie na adres email: %s.", user.getEmail());
  }

  @Override
  @Transactional
  public void activateAccount(String token) {
    final OtaTokenEntity otaToken = otaService.validateToken(OtaType.ACTIVATE_ACCOUNT, token);
    final UserEntity user = otaToken.getUser();
    if (user.getActive()) {
      throw new RestServerException("Konto zostało już aktywowane.");
    }
    otaToken.setUsed(true);
    user.setActive(true);

    log.info("Activated account for user: {}.", user);
    logPersistService.info("Konto z adresem email: %s zostało aktywowane.", user.getEmail());
  }

  @Override
  public RevalidateSessionResDto revalidateSession() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()
      && authentication.getPrincipal() instanceof LoggedUser loggedUser
    ) {
      final UserRole role = loggedUser.userEntity().getRole().getName();
      return new RevalidateSessionResDto(true, role.getLocaleName(), role.name());
    }
    return new RevalidateSessionResDto(false, null, null);
  }

  private GeneratedOta generateActivateAccountOta(UserEntity user) {
    return otaService.generateToken(OtaType.ACTIVATE_ACCOUNT,
      Duration.ofMinutes(otaProperties.getShortExpiredMin()), user);
  }

  private void sendActivateAccountEmail(
    GeneratedOta ota,
    UserEntity user,
    String title,
    MailTemplate template
  ) {
    final String token = ota.entity().getToken();
    final String activationLink = String
      .format("%s/aktywacja/%s", mailProperties.getClientUrl(), token);

    final Context context = new Context();
    context.setVariable("name", user.getFirstName() + " " + user.getLastName());
    context.setVariable("token", token);
    context.setVariable("activationLink", activationLink);
    context.setVariable("tokenExpiration", DateTime.formatSeconds(ota.expiredSeconds()));

    mailService.send(user.getEmail(), title, context, template);
  }
}
