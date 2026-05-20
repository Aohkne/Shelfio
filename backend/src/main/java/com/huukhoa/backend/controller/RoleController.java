package com.huukhoa.backend.controller;

import com.huukhoa.backend.dto.request.PermissionCreationRequest;
import com.huukhoa.backend.dto.request.RoleCreationRequest;
import com.huukhoa.backend.dto.response.BaseApiResponse;
import com.huukhoa.backend.dto.response.PermissionResponse;
import com.huukhoa.backend.dto.response.RoleResponse;
import com.huukhoa.backend.service.PermissionService;
import com.huukhoa.backend.service.RoleService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Role Management", description = "APIs for managing role")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {
    RoleService roleService;

    @Operation(
            summary = "Create Role",
            description = "Creates a new role"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Role created successfully",
                    content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
            )
    })
    @PostMapping()
    BaseApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleCreationRequest request) {
        return BaseApiResponse.<RoleResponse>builder()
                .code(200)
                .result(roleService.createRole(request))
                .build();
    }

    @Operation(
            summary = "Get all roles",
            description = "Retrieves a list of all roles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of roles"
            )
    })
    @GetMapping
    BaseApiResponse<List<RoleResponse>> getAll() {
        return BaseApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .result(roleService.getAll())
                .build();
    }

    @Operation(
            summary = "Delete Role",
            description = "Permanently deletes a role by their NAME"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Role deleted successfully",
                    content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
            )
    })
    @DeleteMapping("/{roleName}")
    BaseApiResponse<String> delete(
            @Parameter(description = "Name of the Role to delete", required = true)
            @PathVariable String roleName
    ) {
        roleService.delete(roleName);
        return BaseApiResponse.<String>builder()
                .code(200)
                .message("Role deleted successfully")
                .build();
    }
}
