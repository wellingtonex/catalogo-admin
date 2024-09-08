package com.fullcycle.admin.catalogo.application.category.retrieve;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class SampleIT {


    private final CreateCategoryUseCase useCase;
    private final CategoryRepository categoryRepository;


    public SampleIT(
            @Autowired CreateCategoryUseCase useCase,
            @Autowired CategoryRepository categoryRepository) {
        this.useCase = useCase;
        this.categoryRepository = categoryRepository;
    }

    @Test
    public void testInjects() {
        assertNotNull(useCase);
        assertNotNull(categoryRepository);
    }
}
