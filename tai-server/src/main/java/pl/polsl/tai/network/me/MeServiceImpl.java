package pl.polsl.tai.network.me;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.tai.domain.address.AddressEntity;
import pl.polsl.tai.domain.book.BookRepository;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;
import pl.polsl.tai.exception.RestServerException;
import pl.polsl.tai.log.LogPersistService;
import pl.polsl.tai.network.me.dto.*;
import pl.polsl.tai.security.LoggedUser;

@Slf4j
@Service
@RequiredArgsConstructor
class MeServiceImpl implements MeService {
	private final LogPersistService logPersistService;

	private final UserRepository userRepository;
	private final BookRepository bookRepository;

	@Override
	public MeDetailsResDto getMeDetails(LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();
		return new MeDetailsResDto(user);
	}

	@Override
	@Transactional
	public UpdatedUserDetailsResDto updateDetails(UpdateUserDetailsReqDto reqDto, LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();

		user.setFirstName(reqDto.getFirstName());
		user.setLastName(reqDto.getLastName());

		final var resDto = new UpdatedUserDetailsResDto(user);
		log.info("Updated user: {} details. Details: {}.", user.getEmail(), resDto);
		logPersistService.info("Użytkownik: %s zmienił imię i/lub nazwisko swojego konta.", user.getEmail());
		return resDto;
	}

	@Override
	@Transactional
	public UserAddressDto updateAddress(UpdateUserAddressReqDto reqDto, LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();
		final AddressEntity address = user.getAddress();

		address.setStreet(reqDto.getStreet());
		address.setCity(reqDto.getCity());
		address.setApartmentNumber(reqDto.getApartmentNumber());
		address.setBuildingNumber(reqDto.getBuildingNumber());

		final var resDto = new UserAddressDto(address);
		log.info("Updated customer: {} address. New address: {}.", user.getEmail(), address);
		logPersistService.info("Klient: %s zmienił dane adresowe swojego konta.", user.getEmail());
		return resDto;
	}

	@Override
	public void deleteAccount(LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();
		if (bookRepository.countAllRentedByUserId(user.getId()) > 0) {
			throw new RestServerException("Nie możesz usunąć konta, gdy masz wypożyczoną przynajmniej jedną książkę.");
		}
		userRepository.delete(user);

		log.info("Customer: {} removed account.", user);
		logPersistService.info("Klient: %s usunął swoje konto.", user.getEmail());
	}
}
