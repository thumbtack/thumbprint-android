package com.thumbtack.thumbprint

import android.text.SpannableStringBuilder
import android.text.Spanned

/**
 * Apply the given action on the [SpannableStringBuilder] with the specified span.
 *
 * Example:
 *
 *     SpannableStringBuilder()
 *         .withSpan(StyleSpan(Typeface.BOLD)) {
 *             append("bold")
 *         }
 *         .append(" no style ")
 *         .withSpan(StyleSpan(Typeface.ITALIC)) {
 *             append("italics")
 *         }
 *         .let { textView.text = it }
 */
inline fun SpannableStringBuilder.withSpan(
    span: Any,
    action: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    return apply {
        val from = length
        action()
        setSpan(span, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

/**
 * Apply the given action on the [SpannableStringBuilder] with multiple spans.
 */
inline fun SpannableStringBuilder.withSpans(
    vararg spans: Any,
    action: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    return apply {
        val from = length
        action()
        spans.forEach { setSpan(it, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
    }
}

/**
 * Apply the given action on the [SpannableStringBuilder] with multiple spans.
 */
inline fun SpannableStringBuilder.withSpans(
    spans: Iterable<Any>,
    action: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    return apply {
        val from = length
        action()
        spans.forEach { setSpan(it, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
    }
}
