package com.huukhoa.backend.book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    UUID id;
    String title;
    String author;
    String isbn;
    String publisher;
    String description;
    int quantity;
    int availableQty;
    LocalDate addedDate;
}
