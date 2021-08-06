package com.thumbtack.thumbprint.views.edittext

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import com.thumbtack.thumbprint.R

/**
 * A single line ThumbprintEditText with no drawables. While you can use
 * [ThumbprintTextInputWithDrawables] without defining any drawables in place of this View,
 * it is recommended to use this View if you do not plan on defining drawables. This is because
 * [ThumbprintTextInputWithDrawables] has tap event listeners which will do unncessary work if no
 * drawables are defined.
 */
class ThumbprintTextInput(
    context: Context,
    attrs: AttributeSet? = null
) : ThumbprintEditTextBase(
    ContextThemeWrapper(context, R.style.ThumbprintBasicTextTheme),
    attrs
)
