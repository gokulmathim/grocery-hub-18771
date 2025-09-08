package org.example.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * PUBLIC_INTERFACE
 * AppSmokeTest
 *
 * Minimal unit test to allow CI to discover and execute at least one test.
 */
class AppSmokeTest {

    @Test
    fun additionWorks() {
        assertEquals(4, 2 + 2)
    }
}
