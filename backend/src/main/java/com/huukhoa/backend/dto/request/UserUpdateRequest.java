package com.huukhoa.backend.dto.request;

import com.huukhoa.backend.dto.response.RoleResponse;
import com.huukhoa.backend.validator.DobConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for updating user information")
public class UserUpdateRequest {

    @Schema(
            description = "Updated full name",
            example = "Updated Fullname"
    )
    String fullname;

    @Schema(
            description = "Updated date of birth",
            example = "2002-02-20"
    )
    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    @Schema(
            description = "User 's role",
            example = "[\"USER\", \"ADMIN\"]"
    )
    List<String> roles;
}
