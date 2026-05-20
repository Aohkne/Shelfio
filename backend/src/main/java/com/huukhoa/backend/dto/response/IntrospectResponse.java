package com.huukhoa.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object for token validation")
public class IntrospectResponse {

    @Schema(
            description = "Token validity status",
            example = "true"
    )
    boolean valid;
}
