package com.huukhoa.backend.controller;

import com.huukhoa.backend.entity.User;
import com.huukhoa.backend.service.UserService;

import com.huukhoa.backend.dto.request.UserCreationRequest;
import com.huukhoa.backend.dto.request.UserUpdateRequest;

import com.huukhoa.backend.dto.response.BaseApiResponse;
import com.huukhoa.backend.dto.response.UserResponse;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Management", description = "APIs for managing users and accounts")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all registered users"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of users"
            )
    })
    @GetMapping
    BaseApiResponse<List<User>> getUsers() {
        return BaseApiResponse.<List<User>>builder()
                .code(200)
                .result(userService.getUsers())
                .build();
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves detailed information about a specific user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = User.class))
            )
    })
    @GetMapping("/{userId}")
    BaseApiResponse<UserResponse> getUser(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable String userId
    ) {
        return BaseApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getUser(userId))
                .build();
    }

    @Operation(
            summary = "Get my profile",
            description = "Retrieves detailed information about my profile"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = User.class))
            )
    })
    @GetMapping("/myProfile")
    BaseApiResponse<UserResponse> getProfile() {
        return BaseApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getProfile())
                .build();
    }

    @Operation(
            summary = "Update user information",
            description = "Updates an existing user's information by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = User.class))
            )
    })
    @PutMapping("/{userId}")
    BaseApiResponse<UserResponse> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable String userId,
            @RequestBody UserUpdateRequest request
    ) {
        return BaseApiResponse.<UserResponse>builder()
                .code(204)
                .result(userService.updateUser(userId, request))
                .build();
    }

    @Operation(
            summary = "Delete user",
            description = "Permanently deletes a user account by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            )
    })
    @DeleteMapping("/{userId}")
    BaseApiResponse<String> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable String userId
    ) {
        userService.deleteUser(userId);
        return BaseApiResponse.<String>builder()
                .code(200)
                .message("User deleted successfully")
                .build();
    }
}