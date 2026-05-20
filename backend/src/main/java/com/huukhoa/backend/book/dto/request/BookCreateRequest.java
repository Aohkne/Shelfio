package com.huukhoa.backend.book.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCreateRequest {

    @NotBlank(message = "Title is required")
    String title;

    @NotBlank(message = "Author is required")
    String author;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^[0-9]{10}$|^[0-9]{13}$", message = "ISBN must be 10 or 13 digits")
    String isbn;

    String publisher;

    String description;

    @Min(value = 1, message = "Quantity must be at least 1")
    int quantity;
}
