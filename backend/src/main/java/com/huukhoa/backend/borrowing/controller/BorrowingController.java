package com.huukhoa.backend.borrowing.controller;

import com.huukhoa.backend.borrowing.dto.request.BorrowingCreateRequest;
import com.huukhoa.backend.borrowing.dto.response.BorrowingResponse;
import com.huukhoa.backend.borrowing.service.BorrowingService;
import com.huukhoa.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Borrowing", description = "Book borrowing and return APIs")
public class BorrowingController {
    BorrowingService borrowingService;

    @Operation(summary = "Borrow a book", description = "Requires LIBRARIAN or ADMIN role. Checks member status, book availability, and duplicates.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Member inactive or book unavailable"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    ApiResponse<BorrowingResponse> createBorrowing(@RequestBody @Valid BorrowingCreateRequest request) {
        return ApiResponse.ok("Book borrowed successfully", borrowingService.createBorrowing(request));
    }

    @Operation(summary = "Return a book", description = "Requires LIBRARIAN or ADMIN role. Marks borrowing as returned or overdue.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Borrowing not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PatchMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    ApiResponse<BorrowingResponse> returnBorrowing(@PathVariable UUID id) {
        return ApiResponse.ok("Book returned successfully", borrowingService.returnBorrowing(id));
    }

    @Operation(summary = "Get overdue borrowings", description = "Requires ADMIN or LIBRARIAN role. Returns all active borrowings past their due date.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Overdue list returned"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    ApiResponse<List<BorrowingResponse>> getOverdue() {
        return ApiResponse.ok(borrowingService.getOverdue());
    }
}