package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.domain.Pagination;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        return save(category);
    }

    @Override
    public void deleteById(final CategoryID id) {
        final String idValue = id.getValue();
        if (categoryRepository.existsById(idValue)) {
            categoryRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID id) {
        return Optional.empty();
    }

    @Override
    public Category update(Category category) {
        return save(category);
    }

    private Category save(Category category) {
        return categoryRepository.save(CategoryJpaEntity.from(category)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }
}
