package com.huukhoa.backend.admin.service;

import com.huukhoa.backend.admin.dto.response.StatsResponse;
import com.huukhoa.backend.book.repository.BookRepository;
import com.huukhoa.backend.borrowing.repository.BorrowingRepository;
import com.huukhoa.backend.common.enums.BorrowingStatus;
import com.huukhoa.backend.common.enums.MemberStatus;
import com.huukhoa.backend.member.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminStatsService {
    BookRepository bookRepository;
    MemberRepository memberRepository;
    BorrowingRepository borrowingRepository;

    public StatsResponse getStats() {
        LocalDate today = LocalDate.now();

        CompletableFuture<Long> totalBooksFuture =
                CompletableFuture.supplyAsync(bookRepository::count);

        CompletableFuture<Long> totalMembersFuture =
                CompletableFuture.supplyAsync(() -> memberRepository.countByStatus(MemberStatus.ACTIVE));

        CompletableFuture<Long> activeBorrowsFuture =
                CompletableFuture.supplyAsync(() -> borrowingRepository.countByStatus(BorrowingStatus.ACTIVE));

        CompletableFuture<Long> overdueBorrowsFuture =
                CompletableFuture.supplyAsync(() -> borrowingRepository.countByStatusAndDueDateBefore(BorrowingStatus.ACTIVE, today));

        CompletableFuture.allOf(totalBooksFuture, totalMembersFuture, activeBorrowsFuture, overdueBorrowsFuture).join();

        return StatsResponse.builder()
                .totalBooks(totalBooksFuture.join())
                .totalMembers(totalMembersFuture.join())
                .activeBorrows(activeBorrowsFuture.join())
                .overdueBorrows(overdueBorrowsFuture.join())
                .build();
    }
}
