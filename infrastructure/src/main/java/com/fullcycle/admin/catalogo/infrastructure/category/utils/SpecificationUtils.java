package com.fullcycle.admin.catalogo.infrastructure.category.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {}

    public static <T>Specification<T> like(final String property, final String value) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(property)), like(value.toUpperCase()));
    }

    private static String like(String value) {
        return "%" + value + "%";
    }
}
