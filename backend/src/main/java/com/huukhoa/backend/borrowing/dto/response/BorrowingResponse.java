package com.huukhoa.backend.borrowing.dto.response;

import com.huukhoa.backend.common.enums.BorrowingStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BorrowingResponse {
    UUID id;
    UUID memberId;
    String memberCode;
    UUID bookId;
    String bookTitle;
    LocalDate borrowDate;
    LocalDate dueDate;
    LocalDate returnDate;
    BorrowingStatus status;
    Long daysOverdue;
}
