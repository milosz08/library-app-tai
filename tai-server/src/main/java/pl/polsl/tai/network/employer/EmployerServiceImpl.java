package pl.polsl.tai.network.employer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.role.RoleEntity;
import pl.polsl.tai.domain.role.RoleRepository;
import pl.polsl.tai.domain.role.UserRole;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.domain.user.UserSpec;
import pl.polsl.tai.dto.DeleteResultResDto;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.exception.NotFoundRestServerException;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.mail.MailProperties;
import pl.polsl.tai.mail.MailService;
import pl.polsl.tai.mail.MailTemplate;
import pl.polsl.tai.network.employer.dto.*;
import pl.polsl.tai.security.LoggedUser;
import pl.polsl.tai.security.ota.GeneratedOta;
import pl.polsl.tai.security.ota.OtaProperties;
import pl.polsl.tai.security.ota.OtaService;
import pl.polsl.tai.util.DateTime;
import pl.polsl.tai.util.SecureRandomGenerator;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class EmployerServiceImpl implements EmployerService {
  private final LogPersistService logPersistService;
  private final PasswordEncoder passwordEncoder;
  private final OtaService otaService;
  private final OtaProperties otaProperties;
  private final MailService mailService;
  private final MailProperties mailProperties;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final OtaTokenRepository otaTokenRepository;

  @Value("${application.first-password.length}")
  private int firstPasswordLength;

  @Override
  public PageableResDto<EmployerRowResDto, UserEntity> getPageableEmployers(
    String email,
    Integer page,
    Integer size
  ) {
    final String emailSafe = email == null ? "" : email;
    final int pageSafe = page == null ? 1 : page;
    final int sizeSafe = size == null ? 10 : size;

    final Specification<UserEntity> spec = Specification
      .where(UserSpec.hasEmail(emailSafe))
      .and(UserSpec.hasRole(UserRole.EMPLOYER));

    final Page<UserEntity> pageable = userRepository
      .findAll(spec, PageRequest.of(pageSafe - 1, sizeSafe));

    final List<EmployerRowResDto> results = pageable.map(EmployerRowResDto::new).toList();
    return new PageableResDto<>(results, pageable);
  }

  @Override
  public void createEmployer(AddEmployerReqDto reqDto, LoggedUser loggedUser) {
    final RoleEntity role = roleRepository
      .findByName(UserRole.EMPLOYER)
      .orElseThrow(RestServerException::new);

    final String temporaryPassword = SecureRandomGenerator.generate(firstPasswordLength);

    final UserEntity employer = UserEntity.builder()
      .firstName(reqDto.getFirstName())
      .lastName(reqDto.getLastName())
      .email(reqDto.getEmail())
      .password(passwordEncoder.encode(temporaryPassword))
      .role(role)
      .build();

    final GeneratedOta ota = generateSetPasswordOta(employer);

    sendMailMessageToActivateAccount(ota, employer, temporaryPassword, "Dane dostępowe do konta",
      MailTemplate.CREATE_EMPLOYER_ACCOUNT);

    userRepository.save(employer);
    otaTokenRepository.save(ota.entity());

    log.info("Create new employer by: {}. Employer data: {}.", loggedUser.getUsername(), reqDto);
    logPersistService.info("Dodano nowego pracownika z adresem email: %s przez administratora: %s.",
      employer.getEmail(), loggedUser.getUsername());
  }

  @Override
  @Transactional
  public UpdateEmployerResDto updateEmployer(
    Long employerId,
    UpdateEmployerReqDto reqDto,
    LoggedUser loggedUser
  ) {
    final UserEntity employer = findEmployer(employerId);

    employer.setFirstName(reqDto.getFirstName());
    employer.setLastName(reqDto.getLastName());

    log.info("Updated employer by: {}. Employer data: {}.", loggedUser.getUsername(), reqDto);
    logPersistService.info(
      "Zaktualizowano dane pracownika z adresem email: %s przez administratora: %s.",
      employer.getEmail(), loggedUser.getUsername()
    );
    return new UpdateEmployerResDto(employer);
  }

  @Override
  @Transactional
  public void firstAccessRegenerateToken(Long employerId, LoggedUser loggedUser) {
    final UserEntity employer = findEmployer(employerId);
    if (employer.getActive()) {
      throw new RestServerException("To konto zostało już aktywowane.");
    }
    final String temporaryPassword = SecureRandomGenerator.generate(firstPasswordLength);
    final GeneratedOta ota = generateSetPasswordOta(employer);
    otaTokenRepository.save(ota.entity());

    sendMailMessageToActivateAccount(ota, employer, temporaryPassword,
      "Ponowne wygenerowanie danych dostępowych do konta",
      MailTemplate.REGENERATE_TOKEN_ACTIVATE_EMPLOYER
    );
    employer.setPassword(passwordEncoder.encode(temporaryPassword));

    log.info("Regenerate first access token for: {} by: {}.", employer, loggedUser.getUsername());
    logPersistService.info(
      "Wygenerowany nowy token pierwszego dostępu dla pracownika: %s przez administratora: %s.",
      employer.getEmail(), loggedUser.getUsername()
    );
  }

  @Override
  @Transactional
  public void firstAccessUpdatePassword(String token, FirstAccessUpdatePasswordReqDto reqDto) {
    final OtaTokenEntity otaToken = otaService.validateToken(OtaType.CHANGE_FIRST_PASSWORD, token);

    final UserEntity employer = otaToken.getUser();
    if (!passwordEncoder.matches(reqDto.getTemporaryPassword(), employer.getPassword())) {
      throw new RestServerException("Wprowadzono nieprawidłowe tymczasowe hasło.");
    }
    employer.setPassword(passwordEncoder.encode(reqDto.getNewPassword()));
    employer.setActive(true);

    otaToken.setUsed(true);

    log.info("Employer: {} changed their default password.", employer);
    logPersistService.info("Pracownik: %s zmienił swoje bazowe hasło i aktywował konto.",
      employer.getEmail());
  }

  @Override
  public DeleteResultResDto deleteEmployers(List<Long> employerIds, LoggedUser loggedUser) {
    List<UserEntity> employers;
    if (employerIds.isEmpty()) {
      employers = userRepository.findAllByRole_Name(UserRole.EMPLOYER);
    } else {
      employers = userRepository.findAllByIdInAndRole_Name(employerIds, UserRole.EMPLOYER);
    }
    userRepository.deleteAll(employers);

    final List<String> deleteEmployers = employers.stream().map(UserEntity::getEmail).toList();

    log.info("Delete employer(s) by: {}. Deleted employer(s) data: {}.", loggedUser.getUsername(),
      deleteEmployers);
    logPersistService.info("Usunięto pracownika/ów: %s przez administratora: %s.", deleteEmployers,
      loggedUser.getUsername());

    return new DeleteResultResDto(employers.size());
  }

  private UserEntity findEmployer(Long employerId) {
    return userRepository
      .findByIdAndRole_Name(employerId, UserRole.EMPLOYER)
      .orElseThrow(() -> new NotFoundRestServerException("Nie znaleziono pracownika."));
  }

  private GeneratedOta generateSetPasswordOta(UserEntity user) {
    return otaService.generateToken(OtaType.CHANGE_FIRST_PASSWORD,
      Duration.ofHours(otaProperties.getLongExpiredHours()), user);
  }

  private void sendMailMessageToActivateAccount(
    GeneratedOta ota,
    UserEntity employer,
    String temporaryPassword,
    String title,
    MailTemplate mailTemplate
  ) {
    final String token = ota.entity().getToken();
    final String activationLink = String.format("%s/pracownik/pierwszy-dostep/%s",
      mailProperties.getClientUrl(), token);

    final Context context = new Context();
    context.setVariable("name", employer.getFirstName() + " " + employer.getLastName());
    context.setVariable("token", token);
    context.setVariable("activationLink", activationLink);
    context.setVariable("tokenExpiration", DateTime.formatSeconds(ota.expiredSeconds()));
    context.setVariable("firstPassword", temporaryPassword);

    mailService.send(employer.getEmail(), title, context, mailTemplate);
  }
}
