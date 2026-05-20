package com.huukhoa.backend.admin.controller;

import com.huukhoa.backend.admin.dto.response.StatsResponse;
import com.huukhoa.backend.admin.service.AdminStatsService;
import com.huukhoa.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Admin dashboard APIs")
public class AdminController {
    AdminStatsService adminStatsService;

    @Operation(summary = "Get dashboard stats", description = "Requires ADMIN role. Returns total books, members, active borrows, and overdue count.")
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<StatsResponse> getStats() {
        return ApiResponse.ok(adminStatsService.getStats());
    }
}
