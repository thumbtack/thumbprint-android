package com.thumbtack.thumbprint

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat

fun tintCompoundDrawablesWithCompatibility(
    color: Int?,
    compoundDrawables: Array<Drawable>,
    compoundDrawablesRelative: Array<Drawable>
) {
    color?.run {
        compoundDrawables.forEach { it.tint(this) }

        // These are set with drawableStart/End
        compoundDrawablesRelative.forEach { it.tint(this) }
    }
}

/**
 * Tints the [Drawable] to the given color when drawn.
 *
 * Be careful using this method! It can create a new Drawable when it calls [DrawableCompat.wrap].
 * Avoid using this method to tint a drawable multiple times as you will wrap the wrapped drawable
 * each time recursively.
 *
 * If you need to be able to tint a Drawable repeatedly, don't use this method. Instead, call
 * [DrawableCompat.wrap] directly, and then cache your wrapped drawable. You can then call
 * DrawableCompat.setTint on it repeatedly safely.
 */
fun Drawable?.tint(color: Int) {
    this?.let { DrawableCompat.setTint(DrawableCompat.wrap(it).mutate(), color) }
}
