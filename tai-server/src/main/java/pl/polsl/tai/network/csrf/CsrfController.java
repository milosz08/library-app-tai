package pl.polsl.tai.network.csrf;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/csrf")
class CsrfController {
	@GetMapping("/token")
	ResponseEntity<CsrfToken> token(CsrfToken csrfToken) {
		return ResponseEntity.ok(csrfToken);
	}
}
