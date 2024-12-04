package pl.polsl.tai.network.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.network.auth.dto.TokenResDto;

import java.util.Optional;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final SecurityContextRepository securityContextRepository;

	@PostMapping("/login")
	ResponseEntity<?> login(
		@RequestBody @Valid LoginReqDto reqDto,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		final Optional<String> token = authService.login(reqDto);
		if (token.isPresent()) {
			return ResponseEntity.ok(new TokenResDto(token.get()));
		}
		securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/register")
	ResponseEntity<TokenResDto> register(@RequestBody @Valid RegisterReqDto reqDto) {
		return ResponseEntity.ok(new TokenResDto(authService.register(reqDto)));
	}

	@PatchMapping("/activate/{token}")
	ResponseEntity<Void> activateAccount(@PathVariable String token) {
		authService.activateAccount(token);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/session/revalidate")
	ResponseEntity<Void> refreshSession() {
		return ResponseEntity.noContent().build();
	}
}

