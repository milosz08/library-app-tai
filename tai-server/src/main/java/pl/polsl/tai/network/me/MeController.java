package pl.polsl.tai.network.me;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.network.me.dto.*;
import pl.polsl.tai.security.LoggedUser;

@RestController
@RequestMapping("/v1/@me")
@RequiredArgsConstructor
class MeController {
  private final MeService meService;

  @GetMapping
  ResponseEntity<MeDetailsResDto> details(@AuthenticationPrincipal LoggedUser loggedUser) {
    return ResponseEntity.ok(meService.getMeDetails(loggedUser));
  }

  @PatchMapping
  ResponseEntity<UpdatedUserDetailsResDto> updateDetails(
    @RequestBody @Valid UpdateUserDetailsReqDto reqDto,
    @AuthenticationPrincipal LoggedUser loggedUser
  ) {
    return ResponseEntity.ok(meService.updateDetails(reqDto, loggedUser));
  }

  @PatchMapping("/address")
  ResponseEntity<UserAddressDto> updateAddress(
    @RequestBody @Valid UpdateUserAddressReqDto reqDto,
    @AuthenticationPrincipal LoggedUser loggedUser
  ) {
    return ResponseEntity.ok(meService.updateAddress(reqDto, loggedUser));
  }

  @DeleteMapping
  ResponseEntity<Void> deleteAccount(
    @AuthenticationPrincipal LoggedUser loggedUser,
    HttpServletRequest req,
    HttpServletResponse res,
    Authentication authentication
  ) {
    meService.deleteAccount(loggedUser);
    final var handler = new SecurityContextLogoutHandler();
    handler.logout(req, res, authentication);
    return ResponseEntity.noContent().build();
  }
}
