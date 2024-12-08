package pl.polsl.tai.network.employer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.domain.user.UserEntity;
import pl.polsl.tai.dto.PageableContainerResDto;
import pl.polsl.tai.network.employer.dto.*;
import pl.polsl.tai.security.LoggedUser;

@RestController
@RequestMapping("/v1/employer")
@RequiredArgsConstructor
class EmployerController {
	private final EmployerService employerService;

	@GetMapping
	ResponseEntity<PageableResDto<EmployerRowResDto, UserEntity>> getPageableEmployers(
		@RequestParam(required = false) String email,
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size
	) {
		return ResponseEntity.ok(employerService.getPageableEmployers(email, page, size));
	}

	@PostMapping
	ResponseEntity<TemporalPasswordWithTokenResDto> createEmployer(
		@RequestBody @Valid AddEmployerReqDto reqDto,
		@AuthenticationPrincipal LoggedUser loggedUser
	) {
		return ResponseEntity.ok(employerService.createEmployer(reqDto, loggedUser));
	}

	@PatchMapping("/{employerId}")
	ResponseEntity<UpdateEmployerResDto> updateEmployer(
		@PathVariable Long employerId,
		@RequestBody @Valid UpdateEmployerReqDto reqDto,
		@AuthenticationPrincipal LoggedUser loggedUser
	) {
		return ResponseEntity.ok(employerService.updateEmployer(employerId, reqDto, loggedUser));
	}

	@PatchMapping("/{employerId}/first/access/regenerate")
	ResponseEntity<TemporalPasswordWithTokenResDto> firstAccessRegenerateToken(
		@PathVariable Long employerId,
		@AuthenticationPrincipal LoggedUser loggedUser
	) {
		return ResponseEntity.ok(employerService.firstAccessRegenerateToken(employerId, loggedUser));
	}

	@PatchMapping("/first/access/{token}/password")
	ResponseEntity<Void> firstAccessUpdatePassword(
		@PathVariable String token,
		@Valid @RequestBody FirstAccessUpdatePasswordReqDto reqDto
	) {
		employerService.firstAccessUpdatePassword(token, reqDto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{employerId}")
	ResponseEntity<Void> deleteEmployer(@PathVariable Long employerId, @AuthenticationPrincipal LoggedUser loggedUser) {
		employerService.deleteEmployers(List.of(employerId), loggedUser);
		return ResponseEntity.noContent().build();
	}
}
