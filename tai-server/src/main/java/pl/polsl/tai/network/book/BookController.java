package pl.polsl.tai.network.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.polsl.tai.domain.book.BookEntity;
import pl.polsl.tai.dto.DeleteResultResDto;
import pl.polsl.tai.dto.PageableResDto;
import pl.polsl.tai.dto.SelectedIdsDto;
import pl.polsl.tai.network.book.dto.AddEditBookReqDto;
import pl.polsl.tai.network.book.dto.BookDetailsResDto;
import pl.polsl.tai.network.book.dto.BookRowResDto;
import pl.polsl.tai.security.LoggedUser;

import java.util.List;

@RestController
@RequestMapping("/v1/book")
@RequiredArgsConstructor
class BookController {
  private final BookService bookService;

  @GetMapping
  ResponseEntity<PageableResDto<BookRowResDto, BookEntity>> getPageableBooks(
    @RequestParam(required = false) String title,
    @RequestParam(required = false) Integer page,
    @RequestParam(required = false) Integer size
  ) {
    return ResponseEntity.ok(bookService.getPageableBooks(title, page, size));
  }

  @GetMapping("/{bookId}")
  ResponseEntity<BookDetailsResDto> getBookDetails(@PathVariable Long bookId) {
    return ResponseEntity.ok(bookService.getBookDetails(bookId));
  }

  @PostMapping
  ResponseEntity<Void> createBook(
    @Valid @RequestBody AddEditBookReqDto reqDto,
    @AuthenticationPrincipal LoggedUser loggedUser
  ) {
    bookService.createBook(reqDto, loggedUser);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{bookId}")
  ResponseEntity<Void> updateBook(
    @PathVariable Long bookId,
    @Valid @RequestBody AddEditBookReqDto reqDto,
    @AuthenticationPrincipal LoggedUser loggedUser
  ) {
    bookService.updateBook(bookId, reqDto, loggedUser);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{bookId}")
  ResponseEntity<Void> deleteBook(
    @PathVariable Long bookId,
    @AuthenticationPrincipal LoggedUser loggedUser
  ) {
    bookService.deleteBooks(List.of(bookId), loggedUser);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/selected")
  ResponseEntity<DeleteResultResDto> deleteSelectedBooks(
    @Valid @RequestBody SelectedIdsDto bookIdsDto,
    @AuthenticationPrincipal LoggedUser loggedUser
  ) {
    return ResponseEntity.ok(bookService.deleteBooks(bookIdsDto.getIds(), loggedUser));
  }

  @DeleteMapping
  ResponseEntity<DeleteResultResDto> deleteAllBooks(
    @AuthenticationPrincipal LoggedUser loggedUser
  ) {
    return ResponseEntity.ok(bookService.deleteBooks(List.of(), loggedUser));
  }
}
