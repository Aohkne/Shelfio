package com.huukhoa.backend.book.service;

import com.huukhoa.backend.book.dto.request.BookCreateRequest;
import com.huukhoa.backend.book.dto.request.BookUpdateRequest;
import com.huukhoa.backend.book.dto.response.BookResponse;
import com.huukhoa.backend.book.entity.Book;
import com.huukhoa.backend.book.mapper.BookMapper;
import com.huukhoa.backend.book.repository.BookRepository;
import com.huukhoa.backend.book.spec.BookSpecification;
import com.huukhoa.backend.borrowing.repository.BorrowingRepository;
import com.huukhoa.backend.common.enums.BorrowingStatus;
import com.huukhoa.backend.common.exception.BadRequestException;
import com.huukhoa.backend.common.exception.ResourceNotFoundException;
import com.huukhoa.backend.enums.ErrorCode;
import com.huukhoa.backend.exception.CustomException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    BookRepository bookRepository;
    BorrowingRepository borrowingRepository;
    BookMapper bookMapper;

    public Page<BookResponse> getBooks(String title, String author, Boolean available, Pageable pageable) {
        Specification<Book> spec = BookSpecification.buildSpec(title, author, available);
        return bookRepository.findAll(spec, pageable).map(bookMapper::toBookResponse);
    }

    public BookResponse getBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
        return bookMapper.toBookResponse(book);
    }

    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new CustomException(ErrorCode.BOOK_ISBN_ALREADY_EXISTS);
        }

        Book book = bookMapper.toBook(request);
        book.setAvailableQty(request.getQuantity());
        book.setAddedDate(LocalDate.now());

        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse updateBook(UUID id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        if (request.getIsbn() != null && !request.getIsbn().equals(book.getIsbn())
                && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new CustomException(ErrorCode.BOOK_ISBN_ALREADY_EXISTS);
        }

        bookMapper.updateBook(book, request);
        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        if (borrowingRepository.existsByBook_IdAndStatus(id, BorrowingStatus.ACTIVE)) {
            throw new CustomException(ErrorCode.BOOK_CURRENTLY_BORROWED);
        }

        bookRepository.delete(book);
    }
}
