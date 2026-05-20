package com.huukhoa.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for logout")
public class LogoutRequest {

    @Schema(
            description = "Access JWT token",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String accessToken;

    @Schema(
            description = "Refresh JWT token",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String refreshToken;
}
