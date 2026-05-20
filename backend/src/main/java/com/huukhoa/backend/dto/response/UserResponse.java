package com.huukhoa.backend.dto.response;

import com.huukhoa.backend.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object containing user information")
public class UserResponse {

    @Schema(
            description = "User unique identifier",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    String id;

    @Schema(
            description = "Username",
            example = "huu_khoa"
    )
    String username;

    @Schema(
            description = "Email address",
            example = "huukhoa@example.com"
    )
    String email;

    @Schema(
            description = "User's full name",
            example = "Le Huu Khoa"
    )
    String fullname;

    @Schema(
            description = "Date of birth",
            example = "2002-02-20"
    )
    LocalDate dob;

    @Schema(
            description = "User 's role",
            example = "[\"USER\", \"ADMIN\"]"
    )
    Set<RoleResponse> roles;
}
