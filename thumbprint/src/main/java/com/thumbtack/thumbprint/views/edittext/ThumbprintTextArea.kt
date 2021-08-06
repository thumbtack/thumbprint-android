package com.thumbtack.thumbprint.views.edittext

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import com.thumbtack.thumbprint.R

/**
 * A multi-line ThumbprintEditText. For iOS and web parity, this is referred to as a TextArea.
 */
class ThumbprintTextArea(
    context: Context,
    attrs: AttributeSet? = null
) : ThumbprintEditTextBase(
    ContextThemeWrapper(context, R.style.ThumbprintTextAreaTheme),
    attrs
) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams = layoutParams.apply {
            height = context.resources.getDimensionPixelSize(R.dimen.text_area_height)
        }
    }
}
