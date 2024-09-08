package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;

import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutput execute(String id) {
        CategoryID categoryID = CategoryID.from(id);
        return categoryGateway.findById(categoryID)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(categoryID));
    }

    private static Supplier<DomainException> notFound(CategoryID categoryId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(categoryId.getValue())));
    }
}
