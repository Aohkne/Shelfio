package com.huukhoa.backend.borrowing.mapper;

import com.huukhoa.backend.borrowing.dto.response.BorrowingResponse;
import com.huukhoa.backend.borrowing.entity.Borrowing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {

    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "member.memberCode", target = "memberCode")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(target = "daysOverdue", ignore = true)
    BorrowingResponse toBorrowingResponse(Borrowing borrowing);
}
