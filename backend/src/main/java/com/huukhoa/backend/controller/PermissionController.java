package com.huukhoa.backend.controller;

import com.huukhoa.backend.dto.request.PermissionCreationRequest;
import com.huukhoa.backend.dto.response.BaseApiResponse;
import com.huukhoa.backend.dto.response.PermissionResponse;
import com.huukhoa.backend.entity.User;
import com.huukhoa.backend.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission Management", description = "APIs for managing permission")
@SecurityRequirement(name = "bearerAuth")
public class PermissionController {
    PermissionService permissionService;

    @Operation(
            summary = "Create Permission",
            description = "Creates a new permission for role"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Permission created successfully",
                    content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
            )
    })
    @PostMapping()
    BaseApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionCreationRequest request) {
        return BaseApiResponse.<PermissionResponse>builder()
                .code(200)
                .result(permissionService.createPermission(request))
                .build();
    }

    @Operation(
            summary = "Get all permissions",
            description = "Retrieves a list of all permissions"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of permissions"
            )
    })
    @GetMapping
    BaseApiResponse<List<PermissionResponse>> getAll() {
        return BaseApiResponse.<List<PermissionResponse>>builder()
                .code(200)
                .result(permissionService.getAll())
                .build();
    }

    @Operation(
            summary = "Delete Permission",
            description = "Permanently deletes a permission by their NAME"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Permission deleted successfully",
                    content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
            )
    })
    @DeleteMapping("/{permissionName}")
    BaseApiResponse<String> delete(
            @Parameter(description = "Name of the permission to delete", required = true)
            @PathVariable String permissionName
    ) {
        permissionService.delete(permissionName);
        return BaseApiResponse.<String>builder()
                .code(200)
                .message("Permission deleted successfully")
                .build();
    }
}
