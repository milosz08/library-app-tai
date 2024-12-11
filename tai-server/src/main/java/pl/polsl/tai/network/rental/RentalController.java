package pl.polsl.tai.network.rental;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.network.rental.dto.LoanBookReqDto;
import pl.polsl.tai.network.rental.dto.RentedBookDetailsResDto;
import pl.polsl.tai.network.rental.dto.RentedBookRowResDto;
import pl.polsl.tai.network.rental.dto.ReturnBookReqDto;
import pl.polsl.tai.security.LoggedUser;

@RestController
@RequestMapping("/v1/rental")
@RequiredArgsConstructor
class RentalController {
	private final RentalService rentalService;

	@GetMapping("/rented")
	ResponseEntity<PageableResDto<RentedBookRowResDto, BookEntity>> getPageableRentedBooks(
		@RequestParam(required = false) String title,
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size,
		@AuthenticationPrincipal LoggedUser loggedUser
	) {
		return ResponseEntity.ok(rentalService.getPageableRentedBooks(title, page, size, loggedUser));
	}

	@GetMapping("/rented/{bookId}")
	ResponseEntity<RentedBookDetailsResDto> getRentedBookDetails(
		@PathVariable Long bookId,
		@AuthenticationPrincipal LoggedUser loggedUser
	) {
		return ResponseEntity.ok(rentalService.getRentedBookDetails(bookId, loggedUser));
	}

	@PatchMapping("/loan")
	ResponseEntity<Void> loanBook(
		@Valid @RequestBody LoanBookReqDto reqDto,
		@AuthenticationPrincipal LoggedUser loggedUser
	) {
		rentalService.loanBook(reqDto, loggedUser);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/return")
	ResponseEntity<Void> returnBook(
		@Valid @RequestBody ReturnBookReqDto reqDto,
		@AuthenticationPrincipal LoggedUser loggedUser
	) {
		rentalService.returnBook(reqDto, loggedUser);
		return ResponseEntity.noContent().build();
	}
}

