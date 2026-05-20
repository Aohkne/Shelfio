package com.huukhoa.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for creating a new permission")
public class PermissionResponse {

    @Schema(
            description = "Permission name (3-50 characters)",
            example = "USER"
    )
    String name;

    @Schema(
            description = "Permission description",
            example = "Basic user permission"
    )
    String description;
}