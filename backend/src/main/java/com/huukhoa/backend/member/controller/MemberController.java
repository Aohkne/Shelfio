package com.huukhoa.backend.member.controller;

import com.huukhoa.backend.common.response.ApiResponse;
import com.huukhoa.backend.member.dto.request.UpdateMemberStatusRequest;
import com.huukhoa.backend.member.dto.response.MemberResponse;
import com.huukhoa.backend.member.service.MemberService;
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
@RequestMapping("/api/members")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Members", description = "Member management APIs")
public class MemberController {
    MemberService memberService;

    @Operation(summary = "Get all members", description = "Requires ADMIN or LIBRARIAN role.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    ApiResponse<Page<MemberResponse>> getMembers(Pageable pageable) {
        return ApiResponse.ok(memberService.getMembers(pageable));
    }

    @Operation(summary = "Get my profile", description = "Returns the authenticated member's own profile.")
    @GetMapping("/me")
    ApiResponse<MemberResponse> getMyProfile() {
        return ApiResponse.ok(memberService.getMyProfile());
    }

    @Operation(summary = "Get member by ID", description = "MEMBER can only view their own profile. ADMIN/LIBRARIAN can view any.")
    @GetMapping("/{id}")
    ApiResponse<MemberResponse> getMember(@PathVariable UUID id) {
        return ApiResponse.ok(memberService.getMember(id));
    }

    @Operation(summary = "Update member status", description = "Requires ADMIN role. Can suspend or reactivate members.")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<MemberResponse> updateStatus(@PathVariable UUID id, @RequestBody @Valid UpdateMemberStatusRequest request) {
        return ApiResponse.ok("Member status updated", memberService.updateStatus(id, request));
    }
}
