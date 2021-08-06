package com.thumbtack.thumbprint.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat

/**
 * Returns the [Drawable] to use for a Thumbprint icon. It does so in the following manner:
 * # If the icon attribute was defined in XML, then load the icon drawable associated
 * with that.
 * # Otherwise, use the [defaultIcon] that was passed in.
 *
 * @param context The [Context] object, used for reading resources and resolving fonts
 * @param defaultIcon The Thumbprint open-sourced default icon from material icons
 * @param iconRef The attribute that an app can override to replace the icon
 */
internal fun getThumbprintIcon(
    context: Context,
    defaultIcon: Drawable?,
    iconRef: Int
): Drawable? {
    val outValue = TypedValue()
    val resolved = context.theme.resolveAttribute(iconRef, outValue, true)
    if (resolved && outValue.resourceId != 0) {
        return ContextCompat.getDrawable(context, outValue.resourceId)
    } else {
        return defaultIcon
    }
}
