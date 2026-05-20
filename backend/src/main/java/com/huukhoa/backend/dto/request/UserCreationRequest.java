package com.huukhoa.backend.dto.request;

import com.huukhoa.backend.validator.DobConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for creating a new user")
public class UserCreationRequest {

    @Schema(
            description = "Username (3-50 characters)",
            example = "huu_khoa",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Size(min = 3, message = "USER_USERNAME_LIMIT_EXCEEDED")
    String username;

    @Schema(
            description = "Email address",
            example = "huukhoa@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "USER_EMAIL_NOT_VALID")
    String email;

    @Schema(
            description = "Password (8-50 characters)",
            example = "Login123@",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Size(min = 8, max = 50, message = "USER_PASSWORD_LIMIT_EXCEEDED")
    String password;

    @Schema(
            description = "User's full name",
            example = "Le Huu Khoa"
    )
    String fullname;

    @Schema(
            description = "Date of birth",
            example = "2002-02-02"
    )
    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;
}