package com.thumbtack.thumbprint.views.edittext

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.utilities.getThumbprintIcon

/**
 * A single line ThumbprintEditText that allows for a custom drawableStart, but not a custom
 * drawableEnd. Instead, the drawableEnd will be a X that can be tapped to clear the current input
 * and focus the View.
 */
class ThumbprintClearableTextInput(
    context: Context,
    attrs: AttributeSet? = null
) : ThumbprintTextInputWithDrawables(
    context,
    attrs
) {

    init {
        val drawable = getThumbprintIcon(
            context = context,
            defaultIcon = ContextCompat.getDrawable(context, R.drawable.default_close_icon),
            iconRef = R.attr.closeSmallIcon
        )
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        compoundDrawables[DRAWABLE_END].setTint(ContextCompat.getColor(context, R.color.gray))
    }

    override fun handleDrawableEndTap() {
        text?.clear()
        super.handleDrawableEndTap()
    }
}
