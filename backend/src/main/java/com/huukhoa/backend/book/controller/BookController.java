package com.huukhoa.backend.book.controller;

import com.huukhoa.backend.book.dto.request.BookCreateRequest;
import com.huukhoa.backend.book.dto.request.BookUpdateRequest;
import com.huukhoa.backend.book.dto.response.BookResponse;
import com.huukhoa.backend.book.service.BookService;
import com.huukhoa.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Books", description = "Book management APIs")
public class BookController {
    BookService bookService;

    @Operation(summary = "Get all books", description = "Public endpoint. Filter by title, author, availability.")
    @GetMapping
    ApiResponse<Page<BookResponse>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Boolean available,
            Pageable pageable) {
        return ApiResponse.ok(bookService.getBooks(title, author, available, pageable));
    }

    @Operation(summary = "Get book by ID", description = "Public endpoint.")
    @GetMapping("/{id}")
    ApiResponse<BookResponse> getBook(@PathVariable UUID id) {
        return ApiResponse.ok(bookService.getBook(id));
    }

    @Operation(summary = "Add a book", description = "Requires ADMIN or LIBRARIAN role.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    ApiResponse<BookResponse> createBook(@RequestBody @Valid BookCreateRequest request) {
        return ApiResponse.ok("Book created successfully", bookService.createBook(request));
    }

    @Operation(summary = "Update a book", description = "Requires ADMIN or LIBRARIAN role.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    ApiResponse<BookResponse> updateBook(@PathVariable UUID id, @RequestBody BookUpdateRequest request) {
        return ApiResponse.ok("Book updated successfully", bookService.updateBook(id, request));
    }

    @Operation(summary = "Delete a book", description = "Requires ADMIN role.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ApiResponse.ok("Book deleted successfully");
    }
}
