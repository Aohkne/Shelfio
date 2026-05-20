package com.huukhoa.backend.book.spec;

import com.huukhoa.backend.book.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    private BookSpecification() {}

    public static Specification<Book> hasTitle(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) ->
                author == null ? null : cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> isAvailable() {
        return (root, query, cb) -> cb.greaterThan(root.get("availableQty"), 0);
    }

    public static Specification<Book> buildSpec(String title, String author, Boolean available) {
        Specification<Book> spec = Specification.where(hasTitle(title))
                .and(hasAuthor(author));

        if (Boolean.TRUE.equals(available)) {
            spec = spec.and(isAvailable());
        }

        return spec;
    }
}
