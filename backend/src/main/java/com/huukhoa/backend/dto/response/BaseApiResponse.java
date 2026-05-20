package com.huukhoa.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public class BaseApiResponse<T> {
    @Schema(
            description = "Response status code",
            example = "200"
    )
    int code = 0;

    @Schema(
            description = "Response message (optional)",
            example = "Success"
    )
    String message;

    @Schema(description = "Response data payload")
    T result;
}
