package com.thumbtack.thumbprint

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.collection.LruCache

object DrawableCache {
    private const val MAX_SIZE = 15

    val cache = LruCache<Int, Drawable>(MAX_SIZE)

    fun get(@DrawableRes drawableResId: Int, @ColorInt color: Int): Drawable? =
        cache.get(drawableResId xor color)

    fun put(@DrawableRes drawableResId: Int, @ColorInt color: Int, drawable: Drawable) {
        cache.put(drawableResId xor color, drawable)
    }
}
