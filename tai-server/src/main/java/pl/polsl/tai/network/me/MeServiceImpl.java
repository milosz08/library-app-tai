package pl.polsl.tai.network.me;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.address.AddressEntity;
import pl.polsl.tai.domain.address.AddressRepository;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.network.me.dto.*;
import pl.polsl.tai.security.LoggedUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeServiceImpl implements MeService {
	private final LogPersistService logPersistService;

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;

	@Override
	public MeDetailsResDto getMeDetails(LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();
		return new MeDetailsResDto(user);
	}

	@Override
	public UpdatedUserDetailsResDto updateDetails(UpdateUserDetailsReqDto reqDto, LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();

		user.setFirstName(reqDto.getFirstName());
		user.setLastName(reqDto.getLastName());
		userRepository.save(user);

		final var resDto = new UpdatedUserDetailsResDto(user);
		log.info("Updated user: {} details. Details: {}.", user.getEmail(), resDto);
		logPersistService.info("Użytkownik: %s zmienił imię i/lub nazwisko swojego konta. Nowe dane: %s.",
			user.getEmail(), resDto);
		return resDto;
	}

	@Override
	public UserAddressDto updateAddress(UpdateUserAddressReqDto reqDto, LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();
		final AddressEntity address = user.getAddress();

		address.setStreet(reqDto.getStreet());
		address.setCity(reqDto.getCity());
		address.setApartmentNumber(reqDto.getApartmentNumber());
		address.setBuildingNumber(reqDto.getBuildingNumber());
		addressRepository.save(address);

		final var resDto = new UserAddressDto(address);
		log.info("Updated customer: {} address. New address: {}.", user.getEmail(), address);
		logPersistService.info("Klient: %s zmienił dane adresowe swojego konta. Nowe dane: %s.", user.getEmail(), resDto);
		return resDto;
	}
}
