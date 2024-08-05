package com.rabebBou.book.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> withOwnerId(Integer ownerId){
        return (root, query, criteriaBuild) -> criteriaBuild.equal(root.get("owner").get("id"), ownerId);
    }
}
