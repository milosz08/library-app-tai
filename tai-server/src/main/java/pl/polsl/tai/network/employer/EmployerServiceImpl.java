package pl.polsl.tai.network.employer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.role.RoleEntity;
import pl.polsl.tai.domain.role.RoleRepository;
import pl.polsl.tai.domain.role.UserRole;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.dto.PageableContainerResDto;
import pl.polsl.tai.exception.NotFoundRestServerException;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.network.employer.dto.*;
import pl.polsl.tai.security.LoggedUser;
import pl.polsl.tai.security.ota.GeneratedOta;
import pl.polsl.tai.security.ota.OtaProperties;
import pl.polsl.tai.security.ota.OtaService;
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

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final OtaTokenRepository otaTokenRepository;

	@Value("${application.first-password.length}")
	private int firstPasswordLength;

	@Override
	public PageableContainerResDto<EmployerRowResDto, UserEntity> getPageableEmployers(Integer page, Integer size) {
		final int pageSafe = page == null ? 1 : page;
		final int sizeSafe = size == null ? 10 : size;

		final Page<UserEntity> pageable = userRepository
			.findAllByRole_Name(PageRequest.of(pageSafe - 1, sizeSafe), UserRole.EMPLOYER);

		final List<EmployerRowResDto> results = pageable.map(EmployerRowResDto::new).toList();
		return new PageableContainerResDto<>(results, pageable);
	}

	@Override
	public TemporalPasswordWithTokenResDto createEmployer(AddEmployerReqDto reqDto, LoggedUser loggedUser) {
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
		userRepository.save(employer);
		otaTokenRepository.save(ota.entity());

		log.info("Create new employer by: {}. Employer data: {}.", loggedUser.getUsername(), reqDto);
		logPersistService.info("Dodano nowego pracownika z adresem email: %s przez administratora: %s.",
			employer.getEmail(), loggedUser.getUsername());

		// This credentials should be sent via email sender but... I suppose we don't have to.
		return new TemporalPasswordWithTokenResDto(ota.entity().getToken(), ota.expiredSeconds(), temporaryPassword);
	}

	@Override
	@Transactional
	public UpdateEmployerResDto updateEmployer(Long id, UpdateEmployerReqDto reqDto, LoggedUser loggedUser) {
		final UserEntity employer = findEmployer(id);

		employer.setFirstName(reqDto.getFirstName());
		employer.setLastName(reqDto.getLastName());

		log.info("Updated employer by: {}. Employer data: {}.", loggedUser.getUsername(), reqDto);
		logPersistService.info("Zaktualizowano dane pracownika z adresem email: %s przez administratora: %s.",
			employer.getEmail(), loggedUser.getUsername());

		return new UpdateEmployerResDto(employer);
	}

	@Override
	@Transactional
	public TemporalPasswordWithTokenResDto firstAccessRegenerateToken(Long id, LoggedUser loggedUser) {
		final UserEntity employer = findEmployer(id);
		if (employer.getActive()) {
			throw new RestServerException("To konto zostało już aktywowane.");
		}
		final String temporaryPassword = SecureRandomGenerator.generate(firstPasswordLength);
		final GeneratedOta ota = generateSetPasswordOta(employer);
		otaTokenRepository.save(ota.entity());

		employer.setPassword(passwordEncoder.encode(temporaryPassword));

		log.info("Regenerate first access token for: {} by: {}.", employer, loggedUser.getUsername());
		logPersistService.info("Wygenerowany nowy token pierwszego dostępu dla pracownika: %s przez administratora: %s.",
			employer.getEmail(), loggedUser.getUsername());

		// This credentials should be sent via email sender but... I suppose we don't have to.
		return new TemporalPasswordWithTokenResDto(ota.entity().getToken(), ota.expiredSeconds(), temporaryPassword);
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
		logPersistService.info("Pracownik: %s zmienił swoje bazowe hasło i aktywował konto.", employer.getEmail());
	}

	@Override
	public void deleteEmployer(Long id, LoggedUser loggedUser) {
		final UserEntity employer = findEmployer(id);

		// TODO: any checks? maybe unset referential id to added books?
		userRepository.delete(employer);

		log.info("Delete employer by: {}. Deleted employer data: {}.", loggedUser.getUsername(), employer);
		logPersistService.info("Usunięto pracownika: %s przez administratora: %s.",
			employer.getEmail(), loggedUser.getUsername());
	}

	private UserEntity findEmployer(Long id) {
		return userRepository
			.findByIdAndRole_Name(id, UserRole.EMPLOYER)
			.orElseThrow(() -> new NotFoundRestServerException("Nie znaleziono pracownika."));
	}

	private GeneratedOta generateSetPasswordOta(UserEntity user) {
		return otaService.generateToken(OtaType.CHANGE_FIRST_PASSWORD,
			Duration.ofHours(otaProperties.getLongExpiredHours()), user);
	}
}
