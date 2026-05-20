package com.huukhoa.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for user authentication")
public class AuthenticationRequest {

    @Schema(
            description = "Username for login",
            example = "huu_khoa",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String username;

    @Schema(
            description = "Password for login",
            example = "Login123@",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String password;
}
