package com.api.library.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.api.library.model.Book;

import jakarta.persistence.criteria.Predicate;

public class BookSpecs {
 public static Specification<Book> filterBy(Integer aisleNumber, String isbn, String title) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (aisleNumber != null) {
                predicates.add(cb.equal(root.get("aisleNumber"), aisleNumber));
            }
            if (isbn != null && !isbn.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("isbn")),
                        "%" + isbn.toLowerCase() + "%"));
            }
            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"));
            }
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
