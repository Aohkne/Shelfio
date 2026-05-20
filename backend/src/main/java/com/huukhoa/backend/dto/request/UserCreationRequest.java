package com.huukhoa.backend.dto.request;

import com.huukhoa.backend.validator.DobConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for creating a new user")
public class UserCreationRequest {

    @Schema(description = "Username (3-50 characters)", example = "huu_khoa", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    String username;

    @Schema(description = "Email address", example = "huukhoa@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    @Schema(description = "Password (8-50 characters)", example = "Login123@", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be 8 to 50 characters")
    String password;

    @Schema(description = "Full name", example = "Le Huu Khoa")
    @NotBlank(message = "Full name is required")
    String fullname;

    @Schema(description = "Date of birth", example = "2002-02-02")
    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;
}