package com.thumbtack.thumbprint.views.edittext

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import androidx.annotation.CallSuper
import com.thumbtack.thumbprint.R

/**
 * A single line ThumbprintEditText with drawables. Contains logic for capturing clicks on
 * drawables (both start and end). Can expose drawable tap events through [.uiEvents()]
 */
open class ThumbprintTextInputWithDrawables(
    context: Context,
    attrs: AttributeSet? = null
) : ThumbprintEditTextBase(
    ContextThemeWrapper(context, R.style.ThumbprintBasicTextWithDrawablesTheme),
    attrs
) {

    var drawableStartListener: (() -> Unit)? = null
    var drawableEndListener: (() -> Unit)? = null

    private var focusOnDrawableTap: Boolean = true

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            intArrayOf(R.attr.focusOnDrawableTap)
        )

        focusOnDrawableTap = typedArray.getBoolean(0, true)
        typedArray.recycle()
    }

    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            compoundDrawables[DRAWABLE_START]?.let {
                if (event.rawX <= compoundPaddingStart + paddingStart) {
                    handleDrawableStartTap()
                    return true
                }
            }

            compoundDrawables[DRAWABLE_END]?.let {
                if (event.rawX >= right - compoundPaddingEnd - paddingEnd) {
                    handleDrawableEndTap()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    @CallSuper
    open fun handleDrawableStartTap() {
        if (focusOnDrawableTap) {
            requestFocus()
            drawableStartListener?.invoke()
        }
    }

    @CallSuper
    open fun handleDrawableEndTap() {
        if (focusOnDrawableTap) {
            requestFocus()
            drawableEndListener?.invoke()
        }
    }

    companion object {
        const val DRAWABLE_START: Int = 0
        const val DRAWABLE_END: Int = 2
    }
}
