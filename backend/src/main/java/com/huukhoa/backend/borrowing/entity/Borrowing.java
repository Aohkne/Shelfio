package com.huukhoa.backend.borrowing.entity;

import com.huukhoa.backend.book.entity.Book;
import com.huukhoa.backend.common.enums.BorrowingStatus;
import com.huukhoa.backend.member.entity.Member;
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
@Table(name = "borrowings")
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    @Column(name = "borrow_date", nullable = false)
    LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    LocalDate dueDate;

    @Column(name = "return_date")
    LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BorrowingStatus status;
}
