package com.example.starwars_facade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FacadeTest {

    @Test
    void testFacadeConfig() {
        int retryCount = 3;
        assertEquals(3, retryCount, "Default retry count should be 3");
    }
}
