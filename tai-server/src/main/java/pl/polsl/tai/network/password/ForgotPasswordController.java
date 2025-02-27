package pl.polsl.tai.network.password;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.network.password.dto.ChangePasswordReqDto;
import pl.polsl.tai.network.password.dto.RequestChangePasswordReqDto;

@RestController
@RequestMapping("/v1/forgot/password")
@RequiredArgsConstructor
class ForgotPasswordController {
  private final ForgotPasswordService forgotPasswordService;

  @PatchMapping("/request")
  ResponseEntity<Void> sendRequestToChangePassword(
    @Valid @RequestBody RequestChangePasswordReqDto reqDto
  ) {
    forgotPasswordService.sendRequestToChangePassword(reqDto);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/renew/{token}")
  ResponseEntity<Void> changePassword(
    @PathVariable String token,
    @Valid @RequestBody ChangePasswordReqDto reqDto
  ) {
    forgotPasswordService.changePassword(token, reqDto);
    return ResponseEntity.noContent().build();
  }
}
