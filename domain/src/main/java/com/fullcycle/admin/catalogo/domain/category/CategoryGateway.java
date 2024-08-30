package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);
    void deleteById(CategoryID id);
    Optional<Category> findById(CategoryID id);
    Category update(CategoryID id);
    Pagination<Category> findAll(CategorySearchQuery query);
}
