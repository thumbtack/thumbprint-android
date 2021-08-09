package com.thumbtack.thumbprint.views.dropdown

import android.widget.Adapter
import androidx.test.core.app.ApplicationProvider
import com.thumbtack.thumbprint.MaterialTestApplication
import com.thumbtack.thumbprint.ViewTreeTestHelper
import kotlinx.android.synthetic.main.thumbprint_dropdown.view.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
class ThumbprintDropdownTest {

    @Test
    fun toggleHasError_updatesErrorTextVisibility() {
        val errorText = "There is an error."
        val dropdown = ThumbprintDropdown(ApplicationProvider.getApplicationContext())
        dropdown.errorText = errorText

        // Establish error isn't visible initially.
        ViewTreeTestHelper.assertTextNotVisible(dropdown, errorText)
        dropdown.hasError = true
        ViewTreeTestHelper.assertTextVisible(dropdown, errorText)
        dropdown.hasError = false
        ViewTreeTestHelper.assertTextNotVisible(dropdown, errorText)
    }

    @Test
    fun toggleIsEnabled_updatesErrorTextVisibility() {
        val errorText = "There is an error."
        val dropdown = ThumbprintDropdown(ApplicationProvider.getApplicationContext())
        dropdown.errorText = errorText
        dropdown.hasError = true
        dropdown.isEnabled = false

        // Establish error isn't visible initially.
        ViewTreeTestHelper.assertTextNotVisible(dropdown, errorText)
        dropdown.isEnabled = true
        ViewTreeTestHelper.assertTextVisible(dropdown, errorText)
        dropdown.isEnabled = false
        ViewTreeTestHelper.assertTextNotVisible(dropdown, errorText)
    }

    @Test
    fun setErrorText_updatesErrorTextDisplayed() {
        val dropdown = ThumbprintDropdown(ApplicationProvider.getApplicationContext())
        dropdown.hasError = true

        val error1 = "This is the first error."
        dropdown.errorText = error1
        ViewTreeTestHelper.assertTextVisible(dropdown, error1)

        val error2 = "This is the second error."
        dropdown.errorText = error2
        ViewTreeTestHelper.assertTextVisible(dropdown, error2)
    }

    @Test
    fun setEntries_updatesSpinnerEntries() {
        val dropdown = ThumbprintDropdown(ApplicationProvider.getApplicationContext())

        val entries = arrayOf<CharSequence>("entry 1", "entry 2")
        dropdown.entries = entries
        assertDataMatches(dropdown.spinner.adapter, entries)

        val updatedEntries = arrayOf<CharSequence>(
            "updated entry 1",
            "updated entry 2",
            "new entry 3"
        )
        dropdown.entries = updatedEntries
        dropdown.spinner.performClick()
        assertDataMatches(dropdown.spinner.adapter, updatedEntries)
    }

    private fun <T> assertDataMatches(adapter: Adapter, data: Array<T>) {
        assertEquals(adapter.count, data.size)

        data.forEachIndexed { index, expected ->
            val actual = adapter.getItem(index)
            assertEquals(
                expected,
                actual,
                "Actual item ($actual) at position ($index) doesn't match expected ($expected)."
            )
        }
    }
}
