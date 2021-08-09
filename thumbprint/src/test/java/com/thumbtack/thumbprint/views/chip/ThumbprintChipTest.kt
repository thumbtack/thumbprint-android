package com.thumbtack.thumbprint.views.chip

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
internal class ThumbprintChipTest {

    @Test
    fun onClick_notifiesListener() {
        var isSelectedPassedToListener = false
        val chip = ThumbprintToggleChip(getApplicationContext()).apply {
            onSelectedChangedListener = { _, isSelected ->
                isSelectedPassedToListener = isSelected
            }
        }

        chip.performClick()
        assertTrue(chip.isSelected)
        assertTrue(isSelectedPassedToListener)

        chip.performClick()
        assertFalse(chip.isSelected)
        assertFalse(isSelectedPassedToListener)
    }
}
