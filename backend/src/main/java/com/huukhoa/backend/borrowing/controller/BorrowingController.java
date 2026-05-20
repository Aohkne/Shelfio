package com.huukhoa.backend.borrowing.controller;

import com.huukhoa.backend.borrowing.dto.request.BorrowingCreateRequest;
import com.huukhoa.backend.borrowing.dto.response.BorrowingResponse;
import com.huukhoa.backend.borrowing.service.BorrowingService;
import com.huukhoa.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Borrowings", description = "Book borrowing management APIs")
public class BorrowingController {
    BorrowingService borrowingService;

    @Operation(summary = "Borrow a book", description = "Requires LIBRARIAN or ADMIN role. Creates a new borrowing record.")
    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    ApiResponse<BorrowingResponse> createBorrowing(@RequestBody @Valid BorrowingCreateRequest request) {
        return ApiResponse.ok("Book borrowed successfully", borrowingService.createBorrowing(request));
    }

    @Operation(summary = "Return a book", description = "Requires LIBRARIAN or ADMIN role. Marks borrowing as returned or overdue.")
    @PatchMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    ApiResponse<BorrowingResponse> returnBorrowing(@PathVariable UUID id) {
        return ApiResponse.ok("Book returned successfully", borrowingService.returnBorrowing(id));
    }

    @Operation(summary = "Get overdue borrowings", description = "Requires ADMIN or LIBRARIAN role. Returns all active borrowings past their due date.")
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    ApiResponse<List<BorrowingResponse>> getOverdue() {
        return ApiResponse.ok(borrowingService.getOverdue());
    }
}
