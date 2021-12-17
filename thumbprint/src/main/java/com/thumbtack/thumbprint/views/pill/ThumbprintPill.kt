package com.thumbtack.thumbprint.views.pill

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.databinding.ThumbprintPillBinding
import com.thumbtack.thumbprint.getDrawableIfDefined
import com.thumbtack.thumbprint.setVisibleIfNonNull

/**
 * Displays an optional, small [pillIconDrawable] with [pillText] within a rounded, colored
 * background
 * [pillColor] will change the color of the background, text, and icon and defaults to
 * gray when no color is specified.
 */
class ThumbprintPill(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding by lazy { ThumbprintPillBinding.bind(this) }

    @LayoutRes
    private val layoutRes = R.layout.thumbprint_pill

    var pillColor: ThumbprintPillColor =
        ThumbprintPillColor.GRAY
        set(value) {
            field = value
            ContextCompat.getDrawable(
                context,
                R.drawable.pill_background_shape
            )?.let {
                binding.pillContainer.background = DrawableCompat.wrap(it)
                DrawableCompat.setTint(it, ContextCompat.getColor(context, value.backgroundColor))
            }
            binding.pillText.setTextColor(
                ContextCompat.getColor(
                    context,
                    value.drawableTint
                )
            )
            ImageViewCompat.setImageTintList(
                binding.pillIcon,
                ColorStateList.valueOf(ContextCompat.getColor(context, pillColor.drawableTint))
            )
        }

    var pillText: CharSequence? = null
        set(value) {
            field = value
            binding.pillText.text = value
            contentDescription = value
        }

    var pillIconDrawable: Drawable? = null
        set(value) {
            field = value
            binding.pillIcon.setImageDrawable(pillIconDrawable)
            binding.pillIcon.setVisibleIfNonNull(pillIconDrawable)
        }

    init {
        LayoutInflater.from(context).inflate(layoutRes, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ThumbprintPillStyleable,
            0,
            0
        ).run {
            try {
                val colorStr = getInt(
                    R.styleable.ThumbprintPillStyleable_pillColor,
                    ThumbprintPillColor.GRAY.attributeValue
                )

                pillColor = when (colorStr) {
                    ThumbprintPillColor.GREEN.attributeValue -> ThumbprintPillColor.GREEN
                    ThumbprintPillColor.BLUE.attributeValue -> ThumbprintPillColor.BLUE
                    ThumbprintPillColor.INDIGO.attributeValue -> ThumbprintPillColor.INDIGO
                    ThumbprintPillColor.PURPLE.attributeValue -> ThumbprintPillColor.PURPLE
                    ThumbprintPillColor.RED.attributeValue -> ThumbprintPillColor.RED
                    ThumbprintPillColor.YELLOW.attributeValue -> ThumbprintPillColor.YELLOW
                    ThumbprintPillColor.GRAY.attributeValue -> ThumbprintPillColor.GRAY
                    else -> ThumbprintPillColor.GRAY
                }
                pillText = getText(R.styleable.ThumbprintPillStyleable_pillText)
                pillIconDrawable = getDrawableIfDefined(
                    context,
                    R.styleable.ThumbprintPillStyleable_pillIcon
                )
            } finally {
                recycle()
            }
        }
    }

    companion object {
        /**
         * [ThumbprintPillColor] defines all possible pill colors that thumbprint supports
         *
         * Note: [ThumbprintPillColor.attributeValue] must match the enum values in
         * [R.attr.pillColor]
         */
        enum class ThumbprintPillColor(
            val attributeValue: Int,
            @ColorRes val backgroundColor: Int,
            @ColorRes val drawableTint: Int
        ) {
            GREEN(0, R.color.tp_green_100, R.color.tp_green_600),
            BLUE(1, R.color.tp_blue_100, R.color.tp_blue_600),
            INDIGO(2, R.color.tp_indigo_100, R.color.tp_indigo_600),
            PURPLE(3, R.color.tp_purple_100, R.color.tp_purple_600),
            RED(4, R.color.tp_red_100, R.color.tp_red_600),
            YELLOW(5, R.color.tp_yellow_100, R.color.tp_yellow_600),
            GRAY(6, R.color.tp_gray_300, R.color.tp_black);
        }
    }
}
