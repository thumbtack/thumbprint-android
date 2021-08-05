package com.thumbtack.thumbprint

import android.view.View

/**
 * Contains a [View] and a value. Used for chaining.
 */
class ViewWithValue<out V : View, out T : Any>(
    val view: V,
    val value: T
) {
    /**
     * Applies the given block to the [View] provided to this [ViewWithValue] with the value.
     */
    fun andThen(block: V.(T) -> Unit) = view.apply { block(value) }.let { this }
}
