package com.huukhoa.backend.borrowing.repository;

import com.huukhoa.backend.borrowing.entity.Borrowing;
import com.huukhoa.backend.common.enums.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, UUID> {
    Optional<Borrowing> findByMember_IdAndBook_IdAndStatus(UUID memberId, UUID bookId, BorrowingStatus status);
    boolean existsByBook_IdAndStatus(UUID bookId, BorrowingStatus status);
    List<Borrowing> findByStatusAndDueDateBefore(BorrowingStatus status, LocalDate date);
    long countByStatus(BorrowingStatus status);
    long countByStatusAndDueDateBefore(BorrowingStatus status, LocalDate date);
}
