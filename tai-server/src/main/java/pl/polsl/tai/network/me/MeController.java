package pl.polsl.tai.network.me;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.tai.network.me.dto.MeDetailsResDto;
import pl.polsl.tai.security.LoggedUser;

@RestController
@RequestMapping("/api/v1/@me")
@RequiredArgsConstructor
public class MeController {
	private final MeService meService;

	@GetMapping("/details")
	ResponseEntity<MeDetailsResDto> details(@AuthenticationPrincipal LoggedUser loggedUser) {
		return ResponseEntity.ok(meService.getMeDetails(loggedUser));
	}
}
