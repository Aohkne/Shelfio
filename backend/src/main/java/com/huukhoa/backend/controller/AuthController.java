package com.huukhoa.backend.controller;

import com.huukhoa.backend.common.response.ApiResponse;
import com.huukhoa.backend.dto.request.*;
import com.huukhoa.backend.dto.response.AuthenticationResponse;
import com.huukhoa.backend.dto.response.RegisterResponse;
import com.huukhoa.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {
    AuthService authService;

    @Operation(summary = "Login", description = "Authenticate with email or username and return JWT tokens")
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ApiResponse.ok("Login successfully", authService.login(request));
    }

    @Operation(summary = "Register", description = "Register a new user account and create member profile")
    @PostMapping("/register")
    ApiResponse<RegisterResponse> register(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.ok("Registration successful", authService.register(request));
    }

    @Operation(summary = "Refresh Token", description = "Get a new access token using a valid refresh token")
    @PostMapping("/refresh-token")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshRequest request) {
        return ApiResponse.ok("Token refreshed", authService.refreshToken(request));
    }

    @Operation(summary = "Logout", description = "Invalidate access and refresh tokens")
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) {
        authService.logout(request);
        return ApiResponse.ok("Logged out successfully");
    }
}