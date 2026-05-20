package com.huukhoa.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for creating a new permission")
public class RoleCreationRequest {

    @Schema(
            description = "Role name (3-50 characters)",
            example = "STAFF",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "ROLE_NAME_REQUIRED")
    @Size(min = 3, max = 50, message = "ROLE_NAME_INVALID")
    String name;

    @Schema(
            description = "Role description",
            example = "Basic user Role",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "ROLE_REQUIRED")
    @Size(max = 255, message = "DESCRIPTION_TOO_LONG")
    String description;

    @Schema(
            description = "List of permission names assigned to this role",
            example = "[\"CREATE_BLOG\", \"UPDATE_BLOG\", \"DELETE_BLOG\"]"
    )
    Set<String> permissions;
}