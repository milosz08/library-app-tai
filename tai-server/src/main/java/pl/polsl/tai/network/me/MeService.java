package pl.polsl.tai.network.me;

import pl.polsl.tai.network.me.dto.*;
import pl.polsl.tai.security.LoggedUser;

interface MeService {
	MeDetailsResDto getMeDetails(LoggedUser loggedUser);

	UpdatedUserDetailsResDto updateDetails(UpdateUserDetailsReqDto reqDto, LoggedUser loggedUser);

	UserAddressDto updateAddress(UpdateUserAddressReqDto reqDto, LoggedUser loggedUser);
}
