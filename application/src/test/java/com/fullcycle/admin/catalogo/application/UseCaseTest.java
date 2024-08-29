package com.fullcycle.admin.catalogo.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UseCaseTest {

    @Test
    public void testCreateUseCase() {
        assertNotNull(new UseCase());
        assertNotNull(new UseCase().execute());
    }
}