package com.fullcycle.admin.catalogo.application.category.retrieve.get.list;

import com.fullcycle.admin.catalogo.domain.Pagination;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery query) {
        return categoryGateway.findAll(query)
                .map(CategoryListOutput::from);
    }
}
