package com.huukhoa.backend.borrowing.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowingCreateRequest {

    @NotNull(message = "Member ID is required")
    UUID memberId;

    @NotNull(message = "Book ID is required")
    UUID bookId;
}
