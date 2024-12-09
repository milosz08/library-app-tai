package pl.polsl.tai.network.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.dto.TokenResDto;
import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.network.auth.dto.RevalidateSessionResDto;
import pl.polsl.tai.security.LoggedUser;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
class AuthController {
	private final AuthService authService;
	private final SecurityContextRepository securityContextRepository;

	@PostMapping("/login")
	ResponseEntity<Map<String, Object>> login(
		@RequestBody @Valid LoginReqDto reqDto,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		final Map<String, Object> results = authService.login(reqDto);
		if (results.containsKey("role")) {
			securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
		}
		return ResponseEntity.ok(results);
	}

	@PostMapping("/register")
	ResponseEntity<TokenResDto> register(@RequestBody @Valid RegisterReqDto reqDto) {
		return ResponseEntity.ok(authService.register(reqDto));
	}

	@PatchMapping("/activate/{token}")
	ResponseEntity<Void> activateAccount(@PathVariable String token) {
		authService.activateAccount(token);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/session/revalidate")
	ResponseEntity<RevalidateSessionResDto> revalidateSession(@AuthenticationPrincipal LoggedUser loggedUser) {
		return ResponseEntity.ok(authService.revalidateSession(loggedUser));
	}
}

