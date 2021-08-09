package com.thumbtack.thumbprint.views.edittext

import android.view.MotionEvent
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.view.MotionEventBuilder
import com.thumbtack.thumbprint.MaterialTestApplication
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
class ThumbprintEditTextTest {

    @Test
    fun toggleHasError_updatesState() {
        val editText = ThumbprintTextInput(getApplicationContext())
        // Establish initial state.
        editText.isDisabled = true
        assertEquals(ThumbprintEditTextBase.STATE.DISABLED, editText.state)

        editText.hasError = true // This should override previously set `isDisabled`.
        assertEquals(ThumbprintEditTextBase.STATE.ERROR, editText.state)

        editText.hasError = false
        assertEquals(ThumbprintEditTextBase.STATE.DEFAULT, editText.state)
    }

    @Test
    fun toggleIsDisabled_updatesState() {
        val editText = ThumbprintTextInput(getApplicationContext())
        // Establish initial state.
        editText.hasError = true
        assertEquals(ThumbprintEditTextBase.STATE.ERROR, editText.state)

        editText.isDisabled = true // This should override previously set `hasError`.
        assertEquals(ThumbprintEditTextBase.STATE.DISABLED, editText.state)

        editText.isDisabled = false
        assertEquals(ThumbprintEditTextBase.STATE.DEFAULT, editText.state)
    }

    @Test
    fun setText_updatesState() {
        val editText = ThumbprintTextInput(getApplicationContext())
        // Establish initial state. No text, no focus.
        assertEquals(ThumbprintEditTextBase.STATE.DEFAULT, editText.state)

        // With text, no focus.
        editText.setText("Blah blah")
        assertEquals(ThumbprintEditTextBase.STATE.UNSELECTED_AND_FILLED, editText.state)

        // With text, with focus.
        editText.requestFocus()
        assertEquals(ThumbprintEditTextBase.STATE.SELECTED_AND_FILLED, editText.state)

        // No text, with focus.
        editText.setText("")
        assertEquals(ThumbprintEditTextBase.STATE.SELECTED_AND_EMPTY, editText.state)
    }

    @Test
    fun tapClearDrawable_clearsInputText_notifiesListener() {
        var clearDrawableClicked = false
        val clearableTextInput = ThumbprintClearableTextInput(getApplicationContext()).apply {
            setText("Blah blah")
            drawableEndListener = { clearDrawableClicked = true }
        }

        assertFalse(clearableTextInput.text.isNullOrEmpty())
        // Simulate drawable tap.
        val drawableEndTouchEvent = MotionEventBuilder.newBuilder()
            .setAction(MotionEvent.ACTION_DOWN)
            .setPointer(clearableTextInput.right.toFloat(), 0f)
            .build()
        clearableTextInput.onTouchEvent(drawableEndTouchEvent)

        assertTrue(clearableTextInput.text.isNullOrEmpty())
        assertTrue(clearDrawableClicked)
    }
}
