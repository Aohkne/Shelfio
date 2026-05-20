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
            description = "JWT token to logout",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTcwOTQ2NzIwMCwiZXhwIjoxNzA5NDcwODAwfQ.signature",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String token;
}
