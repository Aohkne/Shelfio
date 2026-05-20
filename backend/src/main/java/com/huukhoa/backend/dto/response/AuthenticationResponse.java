package com.huukhoa.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object containing authentication token")
public class AuthenticationResponse {
    @Schema(
            description = "Access JWT token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    String accesstoken;

    @Schema(
            description = "Refresh JWT token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    String refreshToken;

    @Schema(
            description = "Authentication status",
            example = "true"
    )
    boolean authenticated;
}
