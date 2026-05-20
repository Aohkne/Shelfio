package com.huukhoa.backend.book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String author;

    @Column(unique = true, nullable = false)
    String isbn;

    String publisher;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(nullable = false)
    int quantity;

    @Column(name = "available_qty", nullable = false)
    int availableQty;

    @Column(name = "added_date", nullable = false)
    LocalDate addedDate;
}
