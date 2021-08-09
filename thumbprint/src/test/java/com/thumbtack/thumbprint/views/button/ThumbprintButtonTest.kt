package com.thumbtack.thumbprint.views.button

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.thumbtack.thumbprint.MaterialTestApplication
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
internal class ThumbprintButtonTest {

    @Test
    fun testEnabledState() {
        val button = ThumbprintButton(getApplicationContext())

        assertTrue(button.isEnabled)
        assertFalse(button.isLoading)

        // Set isLoading true twice to verify original isEnabled will still be preserved after.
        button.isLoading = true
        button.isLoading = true
        assertFalse(button.isEnabled)
        assertTrue(button.isLoading)

        button.isLoading = false
        assertTrue(button.isEnabled)
        assertFalse(button.isLoading)

        button.isEnabled = false
        assertFalse(button.isEnabled)
        assertFalse(button.isLoading)

        button.isLoading = true
        assertFalse(button.isEnabled)
        assertTrue(button.isLoading)

        button.isLoading = false
        assertFalse(button.isEnabled)
        assertFalse(button.isLoading)

        button.isEnabled = true
        assertTrue(button.isEnabled)
        assertFalse(button.isLoading)
    }
}
