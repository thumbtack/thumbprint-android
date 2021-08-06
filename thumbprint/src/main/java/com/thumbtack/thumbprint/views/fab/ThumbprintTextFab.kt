package com.thumbtack.thumbprint.views.fab

import android.content.Context
import android.graphics.Typeface.BOLD
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.TextViewCompat
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.utilities.getThumbprintFont

class ThumbprintTextFab(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatButton(ContextThemeWrapper(context, R.style.Thumbprint_Fab_Text), attrs) {

    enum class Style(
        val attributeValue: Int,
        @DrawableRes val backgroundColorRes: Int,
        @ColorRes val textAndIconColorRes: Int
    ) {
        PRIMARY(
            0,
            R.drawable.text_fab_primary_background,
            R.color.white
        ),
        SECONDARY(
            1,
            R.drawable.text_fab_secondary_background,
            R.color.blue
        );
    }

    var fabStyle: Style = Style.PRIMARY
        set(value) {
            field = value
            applyStyle()
        }

    var icon: Drawable? = null
        set(value) {
            field = value

            value?.let {
                setIconTint()
                setCompoundDrawablesRelativeWithIntrinsicBounds(it, null, null, null)
                compoundDrawablePadding =
                    context.resources.getDimensionPixelSize(R.dimen.tp_space_2)
            } ?: run {
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            }
        }

    init {
        // Note: Need to set these style attributes here because, for some reason, they aren't
        // picked up from the style resource when used with ContextThemeWrapper.
        TextViewCompat.setTextAppearance(this, R.style.Thumbprint_Title6Bold)
        typeface = getThumbprintFont(context, typeface, BOLD)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ThumbprintTextFab,
            0,
            0
        ).apply {
            try {
                val iconRes = getResourceId(R.styleable.ThumbprintTextFab_icon, -1)
                if (iconRes != -1) {
                    icon = ContextCompat.getDrawable(context, iconRes)
                }

                fabStyle = getInt(
                    R.styleable.ThumbprintTextFab_fabStyle,
                    Style.PRIMARY.attributeValue
                ).let {
                    requireNotNull(
                        Style.values().find { type -> type.attributeValue == it }
                    ) { "unexpected fabStyle value: $it" }
                }
            } finally {
                recycle()
            }
        }
    }

    private fun applyStyle() {
        setBackgroundResource(fabStyle.backgroundColorRes)
        val tintColor = ContextCompat.getColor(context, fabStyle.textAndIconColorRes)
        setTextColor(tintColor)
        setIconTint()
    }

    private fun setIconTint() {
        icon?.let {
            val tintColor = ContextCompat.getColor(context, fabStyle.textAndIconColorRes)
            DrawableCompat.setTint(it, tintColor)
        }
    }
}
