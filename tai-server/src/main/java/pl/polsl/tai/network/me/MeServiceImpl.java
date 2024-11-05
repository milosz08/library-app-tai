package pl.polsl.tai.network.me;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.network.me.dto.MeDetailsResDto;
import pl.polsl.tai.security.LoggedUser;

@Service
@RequiredArgsConstructor
public class MeServiceImpl implements MeService {
	@Override
	public MeDetailsResDto getMeDetails(LoggedUser loggedUser) {
		final UserEntity user = loggedUser.userEntity();
		return new MeDetailsResDto(user);
	}
}
