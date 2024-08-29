package com.fullcycle.admin.catalogo.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final Category actualCatetory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCatetory);
        Assertions.assertNotNull(actualCatetory.getId());
        Assertions.assertEquals(expectedName, actualCatetory.getName());
        Assertions.assertEquals(expectedDescription, actualCatetory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCatetory.isActive());
        Assertions.assertNotNull(actualCatetory.getCreatedAt());
        Assertions.assertNotNull(actualCatetory.getCreatedAt());
        Assertions.assertNull(actualCatetory.getDeletedAt());

    }

}