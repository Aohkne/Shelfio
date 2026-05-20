package com.huukhoa.backend.book.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookUpdateRequest {
    String title;
    String author;
    String isbn;
    String publisher;
    String description;
    Integer quantity;
}
