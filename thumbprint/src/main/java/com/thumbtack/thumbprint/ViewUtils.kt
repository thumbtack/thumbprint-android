package com.thumbtack.thumbprint

import android.graphics.Rect
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

/**
 * Sets the visibility of this [View] to [VISIBLE] if [value] is true and [otherwise] (defaults to
 * [GONE]) if [value] is false.
 *
 * @return null if [value] is false. [ViewWithValue] otherwise.
 */
fun <V : View> V.setVisibleIfTrue(
    value: Boolean,
    otherwise: Int = GONE
): ViewWithValue<V, Boolean>? {
    return if (value) {
        this.visibility = VISIBLE
        ViewWithValue(this, value)
    } else {
        this.visibility = otherwise
        null
    }
}

/**
 * Sets the visibility of this [View] to [VISIBLE] if [value] is non-null and [otherwise] (defaults
 * to [GONE]) if [value] is null.
 *
 * @return null if [value] is null. [ViewWithValue] otherwise.
 */
fun <V : View, T : Any> V.setVisibleIfNonNull(
    value: T?,
    otherwise: Int = GONE
): ViewWithValue<V, T>? {
    return if (value != null) {
        this.visibility = VISIBLE
        ViewWithValue(this, value)
    } else {
        this.visibility = otherwise
        null
    }
}

fun ViewGroup.forEachChild(block: (View) -> Unit) {
    (0 until childCount).forEach { block(getChildAt(it)) }
}

fun <V : View> V.runIfVisible(block: (V) -> Unit) {
    if (this.visibility == VISIBLE) {
        block(this)
    }
}

/**
 * Tells if the view is on the screen or within the specified part of the screen if arguments are
 * passed.
 */
fun View.isOnScreen(
    left: Int = 0,
    top: Int = 0,
    right: Int = context.resources.displayMetrics.widthPixels,
    bottom: Int = context.resources.displayMetrics.heightPixels
): Boolean {
    if (!isShown) {
        return false
    }
    val pos = Rect().apply { getGlobalVisibleRect(this) }
    val screen = Rect(left, top, right, bottom)
    return pos.intersect(screen)
}
