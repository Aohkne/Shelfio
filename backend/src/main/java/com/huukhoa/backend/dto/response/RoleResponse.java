package com.huukhoa.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for creating a new permission")
public class RoleResponse {

    @Schema(
            description = "Role name (3-50 characters)",
            example = "STAFF"
    )
    String name;

    @Schema(
            description = "Role description",
            example = "Basic user Role"
    )
    String description;

    @Schema(
            description = "List of permission names assigned to this role",
            example = "[\"CREATE_BLOG\", \"UPDATE_BLOG\", \"DELETE_BLOG\"]"
    )
    Set<PermissionResponse> permissions;
}