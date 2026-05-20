package com.huukhoa.backend.book.mapper;

import com.huukhoa.backend.book.dto.request.BookCreateRequest;
import com.huukhoa.backend.book.dto.request.BookUpdateRequest;
import com.huukhoa.backend.book.dto.response.BookResponse;
import com.huukhoa.backend.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableQty", ignore = true)
    @Mapping(target = "addedDate", ignore = true)
    Book toBook(BookCreateRequest request);

    BookResponse toBookResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableQty", ignore = true)
    @Mapping(target = "addedDate", ignore = true)
    void updateBook(@MappingTarget Book book, BookUpdateRequest request);
}
