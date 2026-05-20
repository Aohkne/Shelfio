package com.huukhoa.backend.dto.request;

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
public class PermissionCreationRequest {

    @Schema(
            description = "Permission name (3-50 characters)",
            example = "UPDATE_DATA",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "PERMISSION_NAME_REQUIRED")
    @Size(min = 3, max = 50, message = "PERMISSION_NAME_INVALID")
    String name;

    @Schema(
            description = "Permission description",
            example = "Update data permission",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "DESCRIPTION_REQUIRED")
    @Size(max = 255, message = "DESCRIPTION_TOO_LONG")
    String description;
}