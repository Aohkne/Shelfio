package com.huukhoa.backend.controller;

import com.huukhoa.backend.dto.request.*;
import com.huukhoa.backend.dto.response.AuthenticationResponse;
import com.huukhoa.backend.dto.response.BaseApiResponse;
import com.huukhoa.backend.dto.response.IntrospectResponse;
import com.huukhoa.backend.entity.User;
import com.huukhoa.backend.service.AuthService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Auth Management", description = "APIs for managing auth")
public class AuthController {
    AuthService authService;

    @Operation(summary = "Login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    BaseApiResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        var result = authService.login(request);
        return BaseApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Login successfully")
                .result(result)
                .build();
    }

    @Operation(
            summary = "Register",
            description = "Creates a new user account with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
            )
    })
    @PostMapping("/register")
    BaseApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        BaseApiResponse<User> apiResponse = new BaseApiResponse<>();
        apiResponse.setResult(authService.register(request));

        return apiResponse;
    }

    @Operation(summary = "Verify Token", description = "Check if JWT token is valid")
    @PostMapping("/verify-token")
    BaseApiResponse<IntrospectResponse> verifyToken(@RequestBody @Valid IntrospectRequest request) {
        var result = authService.verifyToken(request);
        return BaseApiResponse.<IntrospectResponse>builder()
                .code(200)
                .message("Verify Token successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Refresh Token", description = "Refresh Token for user")
    @PostMapping("/refresh-token")
    BaseApiResponse<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshRequest request) {
        var result = authService.refreshToken(request);
        return BaseApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Refresh Token successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Logout", description = "User logout")
    @PostMapping("/logout")
    BaseApiResponse<String> logout(@RequestBody @Valid LogoutRequest request) {
        authService.logout(request);
        return BaseApiResponse.<String>builder()
                .code(200)
                .message("Logout successfully")
                .build();
    }


}