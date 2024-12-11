package pl.polsl.tai.network.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.address.AddressEntity;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.role.RoleEntity;
import pl.polsl.tai.domain.role.RoleRepository;
import pl.polsl.tai.domain.role.UserRole;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.dto.TokenResDto;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.network.auth.dto.RevalidateSessionResDto;
import pl.polsl.tai.security.FirstLoginPasswordAuthenticationToken;
import pl.polsl.tai.security.LoggedUser;
import pl.polsl.tai.security.ota.GeneratedOta;
import pl.polsl.tai.security.ota.OtaProperties;
import pl.polsl.tai.security.ota.OtaService;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final OtaService otaService;
	private final LogPersistService logPersistService;
	private final OtaProperties otaProperties;

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final OtaTokenRepository otaTokenRepository;

	@Override
	public Map<String, Object> login(LoginReqDto reqDto) {
		final Map<String, Object> outputResults = new HashMap<>();
		final var authInputToken = FirstLoginPasswordAuthenticationToken.unauthenticated(
			reqDto.getEmail(),
			reqDto.getPassword()
		);
		final Authentication authentication = authenticationManager.authenticate(authInputToken);
		final UserEntity user = ((LoggedUser) authentication.getPrincipal()).userEntity();
		if (authentication.isAuthenticated() && !user.getActive()) {
			final GeneratedOta ota = generateActivateAccountOta(user);
			otaTokenRepository.save(ota.entity());
			// This OTA token should be sent via email sender but... I suppose we don't have to.
			outputResults.put("token", ota.entity().getToken());
			outputResults.put("expiredSeconds", ota.expiredSeconds());
			return outputResults;
		}
		final SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);

		log.info("User: {} was logged in.", user);
		logPersistService.info("Użytkownik: %s zalogował się do serwisu.", user.getEmail());

		outputResults.put("role", user.getRole().getName().name());
		outputResults.put("roleName", user.getRole().getName().getLocaleName());
		return outputResults;
	}

	@Override
	@Transactional
	public TokenResDto register(RegisterReqDto reqDto) {
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

		user.attachAddress(address);
		userRepository.save(user);

		otaTokenRepository.save(ota.entity());

		log.info("Created new customer account: {}.", user);
		logPersistService.info("Utworzono nowe konto klienckie na adres email: %s.", user.getEmail());
		return new TokenResDto(ota.entity().getToken(), ota.expiredSeconds());
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
	public RevalidateSessionResDto revalidateSession(LoggedUser loggedUser) {
		final UserRole role = loggedUser.userEntity().getRole().getName();
		return new RevalidateSessionResDto(role.getLocaleName(), role.name());
	}

	private GeneratedOta generateActivateAccountOta(UserEntity user) {
		return otaService.generateToken(OtaType.ACTIVATE_ACCOUNT,
			Duration.ofMinutes(otaProperties.getShortExpiredMin()), user);
	}
}
