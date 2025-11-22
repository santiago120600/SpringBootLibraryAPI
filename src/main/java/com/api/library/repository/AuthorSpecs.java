package com.api.library.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import com.api.library.model.Author;

public class AuthorSpecs {

    public static Specification<Author> filterBy(String firstName, String lastName) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("firstName")),
                        "%" + firstName.toLowerCase() + "%"));
            }
            if (lastName != null && !lastName.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("lastName")),
                        "%" + lastName.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
