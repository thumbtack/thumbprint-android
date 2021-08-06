package com.thumbtack.thumbprint

import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.thumbtack.thumbprint.utilities.getThumbprintFont

/**
 * This is just a concrete implementation of [ClickableSpan] that takes a listener.
 */
open class ClickListenerSpan(var listener: ((View) -> Unit)? = null) : ClickableSpan() {
    override fun onClick(widget: View) {
        listener?.invoke(widget)
    }
}

/**
 * Removes the underline from a ClickListenerSpan
 */
class LinkSpan(var onClick: ((View) -> Unit)? = null) : ClickListenerSpan(onClick) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}

class BoldColoredLinkSpan(
    val context: Context,
    val color: Int,
    val isUnderlineText: Boolean = true,
    onClick: ((View) -> Unit)? = null
) : ClickListenerSpan(onClick) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.typeface = getThumbprintFont(context, ds.typeface, BOLD)
        ds.isUnderlineText = isUnderlineText
    }
}
