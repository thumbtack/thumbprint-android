package com.thumbtack.thumbprint

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.thumbtack.thumbprint.AlignedImageSpan.Companion.ALIGN_TEXT_CENTER

fun TypedArray.getDrawableResId(@StyleableRes styleableRes: Int): Int? =
    takeIf { it.hasValue(styleableRes) }
        ?.getResourceId(styleableRes, -1)

fun TypedArray.getDrawableIfDefined(
    context: Context,
    @StyleableRes styleableRes: Int,
    @ColorInt color: Int? = null
): Drawable? =
    getDrawableIfDefined(context, getDrawableResId(styleableRes), color)

fun getDrawableIfDefined(
    context: Context,
    @DrawableRes resId: Int?,
    @ColorInt color: Int? = null
) = resId
    ?.let {
        if (color == null) {
            // No color set, just get the original drawable
            ContextCompat.getDrawable(context, resId)
        } else {
            // Color set, check the cache to see if it's available
            DrawableCache.get(resId, color) ?: run {
                // Not in cache, so just get the original and save the tinted version into cache
                ContextCompat.getDrawable(context, resId)
                    ?.let { original ->
                        DrawableCompat.wrap(original.mutate())
                            .also { DrawableCompat.setTint(it, color) }
                    }
                    ?.also { DrawableCache.put(resId, color, it) }
            }
        }
    }
    ?.apply { setBounds(0, 0, intrinsicWidth, intrinsicHeight) }

/**
 * Updates a [TextView]'s text to contain the drawables specified by the given attributes inline in
 * spans.
 *
 * Note that these inline drawables are truly inline within the text. This means that if the intent
 * is to have left and/or right drawables be vertically centered in the view, the text must only
 * span a single line. Additionally--as again the drawables are inline in the text--right inline
 * drawables could be truncated if using ellipsize.
 */
fun TextView.updateTextWithTintedInlineDrawables(
    withDrawablesAttributes: WithDrawablesAttributes?
) {
    withDrawablesAttributes?.let {
        tintInlineDrawables(withDrawablesAttributes)
        text = updateCharSequenceWithInlineDrawables(withDrawablesAttributes, text)
    }
}

/**
 * Updates a [TextView]'s text to contain the drawables specified by the given attributes inline in
 * spans.
 *
 * Note that these inline drawables are truly inline within the text. This means that if the intent
 * is to have left and/or right drawables be vertically centered in the view, the text must only
 * span a single line. Additionally--as again the drawables are inline in the text--right inline
 * drawables could be truncated if using ellipsize.
 */
fun TextView.updateTextWithTintedInlineDrawablesLimited(
    withDrawablesAttributes: WithDrawablesAttributes?
) {
    withDrawablesAttributes?.let {
        tintInlineDrawables(withDrawablesAttributes)
        text = updateCharSequenceWithInlineDrawablesLimited(
            withDrawablesAttributes,
            text,
            context.resources.getDimensionPixelSize(R.dimen.space_1)
        )
    }
}

fun TextView.tintInlineDrawables(withDrawablesAttributes: WithDrawablesAttributes) {
    withDrawablesAttributes.color = when {
        withDrawablesAttributes.drawableTintCompat != null ->
            withDrawablesAttributes.drawableTintCompat
        withDrawablesAttributes.tintDrawablesToTextColor ->
            textColors.getColorForState(drawableState, textColors.defaultColor)
        else -> null
    }
}

/**
 * Transforms text
 */
fun updateCharSequenceWithInlineDrawablesLimited(
    withDrawablesAttributes: WithDrawablesAttributes?,
    text: CharSequence?,
    padding: Int
): CharSequence? {
    return withDrawablesAttributes?.let { attrs ->
        SpannableStringBuilder().apply {
            val inlineDrawableLeft = attrs.inlineDrawableLeft
            val inlineDrawableRight = attrs.inlineDrawableRight
            val trimmed = text?.trim() ?: ""

            inlineDrawableLeft?.let {
                appendImageSpan(
                    drawable = it,
                    paddingRight = padding
                )
            }
            append(trimmed)
            inlineDrawableRight?.let {
                appendImageSpan(
                    drawable = it,
                    paddingLeft = padding
                )
            }
        }
    } ?: text
}

/**
 * Transforms text
 */
fun updateCharSequenceWithInlineDrawables(
    withDrawablesAttributes: WithDrawablesAttributes?,
    text: CharSequence?
): CharSequence? {
    return withDrawablesAttributes?.let { attrs ->
        val inlineDrawableLeft = attrs.inlineDrawableLeft
        val inlineDrawableRight = attrs.inlineDrawableRight
        val inlineDrawableTop = attrs.inlineDrawableTop
        val inlineDrawableBottom = attrs.inlineDrawableBottom
        val inlineDrawablePadding = attrs.inlineDrawablePadding

        val trimmed = text?.trim() ?: ""

        SpannableStringBuilder().apply {
            // Note that the below order is mandatory.
            inlineDrawableTop?.let {
                appendImageSpan(
                    drawable = it,
                    paddingBottom = inlineDrawablePadding
                )
                append('\n')
            }

            inlineDrawableLeft?.let {
                appendImageSpan(
                    drawable = it,
                    paddingRight = inlineDrawablePadding
                )
            }

            append(trimmed)

            inlineDrawableRight?.let {
                appendImageSpan(
                    drawable = it,
                    paddingLeft = inlineDrawablePadding
                )
            }

            inlineDrawableBottom?.let {
                append('\n')

                appendImageSpan(
                    drawable = it,
                    paddingTop = inlineDrawablePadding
                )
            }
        }
    } ?: text
}

private const val PLACEHOLDER_CHAR = ' '

fun SpannableStringBuilder.appendImageSpan(
    drawable: Drawable?,
    paddingLeft: Int = 0,
    paddingTop: Int = 0,
    paddingRight: Int = 0,
    paddingBottom: Int = 0
) {
    drawable?.let {
        withSpan(
            AlignedImageSpan(
                it,
                ALIGN_TEXT_CENTER,
                paddingLeft,
                paddingTop,
                paddingRight,
                paddingBottom
            )
        ) {
            append(PLACEHOLDER_CHAR)
        }
    }
}

fun TypedArray.getColorIfDefined(index: Int): Int? {
    return runCatching {
        this
            .takeIf { it.hasValue(index) }
            ?.getColor(index, 0)
            ?.takeIf { it != 0 }
    }.getOrNull()
}
