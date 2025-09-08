package org.example.app

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * PUBLIC_INTERFACE
 * AppJUnit4SmokeTest
 *
 * JUnit4 test to ensure unit test discovery works in CI.
 */
class AppJUnit4SmokeTest {

    @Test
    fun additionWorks() {
        assertEquals(4, 2 + 2)
    }
}
