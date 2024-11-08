package pl.polsl.tai.network.me;

import jakarta.validation.Valid;
import pl.polsl.tai.network.me.dto.*;
import pl.polsl.tai.security.LoggedUser;

interface MeService {
	MeDetailsResDto getMeDetails(LoggedUser loggedUser);

	UpdatedUserDetailsResDto updateDetails(@Valid UpdateUserDetailsReqDto reqDto, LoggedUser loggedUser);

	UserAddressDto updateAddress(@Valid UpdateUserAddressReqDto reqDto, LoggedUser loggedUser);
}
