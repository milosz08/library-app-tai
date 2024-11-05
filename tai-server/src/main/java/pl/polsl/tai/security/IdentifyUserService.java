package pl.polsl.tai.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
class IdentifyUserService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final UserEntity user = userRepository
			.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika."));
		return new LoggedUser(user);
	}
}
