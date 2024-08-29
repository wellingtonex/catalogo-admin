package com.fullcycle.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    public void testMain() {
        assertNotNull(new Main());
        Main.main(new String[]{});
    }

}