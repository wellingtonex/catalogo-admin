package com.fullcycle.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.AbstractEnvironment;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("integrationTest")
class MainTest {

    @Test
    public void testMain() {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test-integration");
        assertNotNull(new Main());
        Main.main(new String[]{});
    }

}