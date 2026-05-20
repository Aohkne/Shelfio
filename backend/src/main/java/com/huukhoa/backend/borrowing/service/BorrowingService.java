package com.huukhoa.backend.borrowing.service;

import com.huukhoa.backend.book.entity.Book;
import com.huukhoa.backend.book.repository.BookRepository;
import com.huukhoa.backend.borrowing.dto.request.BorrowingCreateRequest;
import com.huukhoa.backend.borrowing.dto.response.BorrowingResponse;
import com.huukhoa.backend.borrowing.entity.Borrowing;
import com.huukhoa.backend.borrowing.mapper.BorrowingMapper;
import com.huukhoa.backend.borrowing.repository.BorrowingRepository;
import com.huukhoa.backend.common.enums.BorrowingStatus;
import com.huukhoa.backend.common.enums.MemberStatus;
import com.huukhoa.backend.enums.ErrorCode;
import com.huukhoa.backend.exception.CustomException;
import com.huukhoa.backend.member.entity.Member;
import com.huukhoa.backend.member.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowingService {
    BorrowingRepository borrowingRepository;
    MemberRepository memberRepository;
    BookRepository bookRepository;
    BorrowingMapper borrowingMapper;

    @Transactional
    public BorrowingResponse createBorrowing(BorrowingCreateRequest request) {
        // Check member exists and is ACTIVE
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (member.getStatus() == MemberStatus.SUSPENDED) {
            throw new CustomException(ErrorCode.MEMBER_SUSPENDED);
        }

        // Check book exists and has available copies
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
        if (book.getAvailableQty() <= 0) {
            throw new CustomException(ErrorCode.BOOK_NOT_AVAILABLE);
        }

        // Check member is not already borrowing this book
        borrowingRepository.findByMember_IdAndBook_IdAndStatus(
                request.getMemberId(), request.getBookId(), BorrowingStatus.ACTIVE)
                .ifPresent(b -> { throw new CustomException(ErrorCode.MEMBER_ALREADY_BORROWING); });

        // Create borrowing
        LocalDate today = LocalDate.now();
        Borrowing borrowing = Borrowing.builder()
                .member(member)
                .book(book)
                .borrowDate(today)
                .dueDate(today.plusDays(14))
                .status(BorrowingStatus.ACTIVE)
                .build();

        borrowingRepository.save(borrowing);

        // Decrease available quantity
        book.setAvailableQty(book.getAvailableQty() - 1);
        bookRepository.save(book);

        return borrowingMapper.toBorrowingResponse(borrowing);
    }

    @Transactional
    public BorrowingResponse returnBorrowing(UUID id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BORROWING_NOT_FOUND));

        if (borrowing.getStatus() != BorrowingStatus.ACTIVE) {
            throw new CustomException(ErrorCode.BORROWING_ALREADY_RETURNED);
        }

        LocalDate today = LocalDate.now();
        borrowing.setReturnDate(today);
        borrowing.setStatus(today.isAfter(borrowing.getDueDate()) ? BorrowingStatus.OVERDUE : BorrowingStatus.RETURNED);

        // Increase available quantity
        Book book = borrowing.getBook();
        book.setAvailableQty(book.getAvailableQty() + 1);
        bookRepository.save(book);

        Borrowing saved = borrowingRepository.save(borrowing);
        BorrowingResponse response = borrowingMapper.toBorrowingResponse(saved);

        // Calculate days overdue if applicable
        if (saved.getStatus() == BorrowingStatus.OVERDUE) {
            long daysOverdue = ChronoUnit.DAYS.between(saved.getDueDate(), today);
            response.setDaysOverdue(daysOverdue);
        }

        return response;
    }

    public List<BorrowingResponse> getOverdue() {
        LocalDate today = LocalDate.now();
        return borrowingRepository.findByStatusAndDueDateBefore(BorrowingStatus.ACTIVE, today)
                .stream()
                .map(borrowing -> {
                    BorrowingResponse response = borrowingMapper.toBorrowingResponse(borrowing);
                    long daysOverdue = ChronoUnit.DAYS.between(borrowing.getDueDate(), today);
                    response.setDaysOverdue(daysOverdue);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
