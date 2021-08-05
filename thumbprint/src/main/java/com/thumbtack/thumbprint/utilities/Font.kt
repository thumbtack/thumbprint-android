package com.thumbtack.thumbprint.utilities

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.TypefaceCompat
import com.thumbtack.thumbprint.R

/**
 * Returns the [Typeface] to use for a Thumbprint component. It does so in the following manner:
 * # If the [R.attr.thumbprintFont] attribute was defined in XML, then load the @font associated
 * with that.
 * # Otherwise, use the [currentTypeface] that was passed in. If this value is null, then the
 * default font of the system is used.
 * # Finally, apply the [style] that was passed in, or just return the font as-is if [style] is null
 *
 * @param context The [Context] object, used for reading resources and resolving fonts
 * @param currentTypeface The current typeface of the Thumbprint component (can be null).
 * @param style The style to apply to the [Typeface]. If null is supplied, then no additional
 *     styling is applied.
 */
internal fun getThumbprintFont(
    context: Context,
    currentTypeface: Typeface?,
    style: Int? = null
): Typeface? {
    var baseTypeface = currentTypeface
    val outValue = TypedValue()
    val resolved = context.theme.resolveAttribute(R.attr.thumbprintFont, outValue, true)
    if (resolved && outValue.resourceId != 0) {
        baseTypeface = ResourcesCompat.getFont(context, outValue.resourceId)
    }
    if (style != null) {
        return TypefaceCompat.create(context, baseTypeface, style)
    }
    return baseTypeface
}
