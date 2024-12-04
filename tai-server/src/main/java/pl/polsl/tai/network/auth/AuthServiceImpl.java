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
import pl.polsl.tai.domain.address.AddressEntity;
import pl.polsl.tai.domain.address.AddressRepository;
import pl.polsl.tai.domain.ota.OtaTokenEntity;
import pl.polsl.tai.domain.ota.OtaTokenRepository;
import pl.polsl.tai.domain.ota.OtaType;
import pl.polsl.tai.domain.role.RoleEntity;
import pl.polsl.tai.domain.role.RoleRepository;
import pl.polsl.tai.domain.role.UserRole;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.security.LoggedUser;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final OtaService otaService;

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final RoleRepository roleRepository;
	private final OtaTokenRepository otaTokenRepository;

	@Override
	public Optional<String> login(LoginReqDto reqDto) {
		final var authInputToken = UsernamePasswordAuthenticationToken.unauthenticated(
			reqDto.getEmail(),
			reqDto.getPassword()
		);
		final Authentication authentication = authenticationManager.authenticate(authInputToken);
		final UserEntity user = ((LoggedUser) authentication.getPrincipal()).userEntity();
		if (authentication.isAuthenticated() && !user.getActive()) {
			final OtaTokenEntity otaToken = otaService.generateToken(OtaType.ACTIVATE_ACCOUNT, user);
			otaTokenRepository.save(otaToken);
			return Optional.of(otaToken.getToken());
		}
		final SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		log.info("User: {} was logged in", user);
		return Optional.empty();
	}

	@Override
	@Transactional
	public String register(RegisterReqDto reqDto) {
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

		final OtaTokenEntity otaToken = otaService.generateToken(OtaType.ACTIVATE_ACCOUNT, user);
		userRepository.save(user);
		addressRepository.save(address);
		otaTokenRepository.save(otaToken);

		log.info("Created new customer account: {}", user);
		return otaToken.getToken();
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

		log.info("Activated account for user: {}", user);
	}
}
