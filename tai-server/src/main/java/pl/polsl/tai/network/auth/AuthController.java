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
import pl.polsl.tai.network.auth.dto.LoginReqDto;
import pl.polsl.tai.network.auth.dto.LoginResDto;
import pl.polsl.tai.network.auth.dto.RegisterReqDto;
import pl.polsl.tai.network.auth.dto.RevalidateSessionResDto;
import pl.polsl.tai.security.LoggedUser;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
class AuthController {
  private final AuthService authService;
  private final SecurityContextRepository securityContextRepository;

  @PostMapping("/login")
  ResponseEntity<LoginResDto> login(
    @RequestBody @Valid LoginReqDto reqDto,
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    final LoginResDto resDto = authService.login(reqDto);
    if (resDto.activated()) {
      securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
    }
    return ResponseEntity.ok(resDto);
  }

  @PostMapping("/register")
  ResponseEntity<Void> register(@RequestBody @Valid RegisterReqDto reqDto) {
    authService.register(reqDto);
    return ResponseEntity.noContent().build();
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

