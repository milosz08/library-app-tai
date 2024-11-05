package pl.polsl.tai.network.me;

import pl.polsl.tai.network.me.dto.MeDetailsResDto;
import pl.polsl.tai.security.LoggedUser;

interface MeService {
	MeDetailsResDto getMeDetails(LoggedUser loggedUser);
}
