package com.thumbtack.thumbprint

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes

/**
 * Wraps [R.styleable.WithDrawables], parsing [styledAttributes] into the class fields.
 */
class WithDrawablesAttributes(
    val context: Context,
    styledAttributes: TypedArray,
    @ColorInt var color: Int? = null
) {

    @DrawableRes
    var fallbackInlineDrawableLeftResId: Int? = null

    @DrawableRes
    var fallbackInlineDrawableRightResId: Int? = null

    @DrawableRes
    var fallbackInlineDrawableTopResId: Int? = null

    @DrawableRes
    var fallbackInlineDrawableBottomResId: Int? = null

    @DimenRes
    var fallbackInlineDrawablePaddingRes: Int? = null

    private val inlineDrawableLeftResId =
        styledAttributes.getDrawableResId(R.styleable.WithDrawables_inlineDrawableLeft)
    private val inlineDrawableRightResId =
        styledAttributes.getDrawableResId(R.styleable.WithDrawables_inlineDrawableRight)
    private val inlineDrawableTopResId =
        styledAttributes.getDrawableResId(R.styleable.WithDrawables_inlineDrawableTop)
    private val inlineDrawableBottomResId =
        styledAttributes.getDrawableResId(R.styleable.WithDrawables_inlineDrawableBottom)

    private val internalInlineDrawablePadding: Int? = styledAttributes
        .takeIf { it.hasValue(R.styleable.WithDrawables_inlineDrawablePadding) }
        ?.getDimensionPixelSize(R.styleable.WithDrawables_inlineDrawablePadding, 0)

    val inlineDrawableLeft
        get() = getDrawableIfDefined(
            context,
            inlineDrawableLeftResId ?: fallbackInlineDrawableLeftResId,
            color
        )

    val inlineDrawableRight
        get() = getDrawableIfDefined(
            context,
            inlineDrawableRightResId ?: fallbackInlineDrawableRightResId,
            color
        )

    val inlineDrawableTop
        get() = getDrawableIfDefined(
            context,
            inlineDrawableTopResId ?: fallbackInlineDrawableTopResId,
            color
        )

    val inlineDrawableBottom
        get() = getDrawableIfDefined(
            context,
            inlineDrawableBottomResId ?: fallbackInlineDrawableBottomResId,
            color
        )

    val inlineDrawablePadding: Int
        get() = internalInlineDrawablePadding
            ?: fallbackInlineDrawablePaddingRes?.let { context.resources.getDimensionPixelSize(it) }
            ?: 0

    val tintDrawablesToTextColor: Boolean = styledAttributes
        .takeIf { it.hasValue(R.styleable.WithDrawables_tintDrawablesToTextColor) }
        ?.getBoolean(R.styleable.WithDrawables_tintDrawablesToTextColor, true)
        ?: true

    val drawableTintCompat = styledAttributes
        .takeIf { it.hasValue(R.styleable.WithDrawables_drawableTintCompat) }
        ?.getColorIfDefined(R.styleable.WithDrawables_drawableTintCompat)

    companion object {
        fun readAttributes(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int = 0
        ): WithDrawablesAttributes {
            val styledAttributes: TypedArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.WithDrawables,
                    defStyleAttr,
                    0
                )

            val withDrawableAttributes =
                WithDrawablesAttributes(context, styledAttributes)

            styledAttributes.recycle()

            return withDrawableAttributes
        }
    }
}
