package com.fullcycle.admin.catalogo.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    public void testNewCategory() {
        Category category = new Category();
        assertNotNull(category);
    }

}