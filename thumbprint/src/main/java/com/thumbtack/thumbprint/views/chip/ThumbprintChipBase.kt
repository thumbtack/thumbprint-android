package com.thumbtack.thumbprint.views.chip

import android.content.Context
import android.graphics.Typeface.BOLD
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.utilities.getThumbprintFont

/**
 * Abstract parent class for Thumbprint chips.
 *
 * You can set the default checked state in xml with the attribute app:isSelected.
 *
 * A [ThumbprintToggleChip] behaves the same way as checkbox and should be used for a set of
 * options where more than one option can be selected.
 *
 * A [ThumbprintFilterChip] behaves like a button and should be used for filtering options.
 */
abstract class ThumbprintChipBase(
    context: Context,
    attrs: AttributeSet? = null,
    private val chipType: ThumbprintChipType
) : AppCompatTextView(ContextThemeWrapper(context, R.style.Thumbprint_Chip), attrs) {

    /**
     * Whether or not the chip is currently in the selected state
     *
     * Override the jvm names to avoid colliding with TextView's function setSelected and
     * View's isSelected function. Perhaps because they are from different classes, simply adding
     * `override` doesn't seem to work.
     */
    @get:JvmName("getIsSelected")
    var isSelected: Boolean = false
        @JvmName("setIsSelected")
        set(value) {
            if (value != field) {
                onSelectedChangedListener?.invoke(this, value)
            }
            field = value
            updateState()
        }

    /**
     * A listener that is called when the isChecked state changes.
     */
    var onSelectedChangedListener: ((view: ThumbprintChipBase, newState: Boolean) -> Unit)? = null

    init {
        applyTextStyling(context, attrs)
        // Filter chips have the same text color regardless of state, so set that here instead of in
        // updateState()
        if (chipType == ThumbprintChipType.FILTER) {
            setTextColor(ContextCompat.getColor(context, R.color.tp_blue))
        }
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ThumbprintChipBase,
            NO_DEFAULT_STYLE_ATTRIBUTE,
            NO_DEFAULT_STYLE_RESOURCE
        ).apply {
            try {
                isSelected = getBoolean(R.styleable.ThumbprintChipBase_isSelected, false)
            } finally {
                recycle()
            }
        }

        updateState()
        setOnClickListener { isSelected = !isSelected }
    }

    private fun updateState() {
        when {
            chipType == ThumbprintChipType.TOGGLE && isSelected -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.chip_toggle_selected
                )
                setTextColor(ContextCompat.getColor(context, R.color.tp_white))
            }
            chipType == ThumbprintChipType.TOGGLE && !isSelected -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.chip_toggle_unselected
                )
                setTextColor(ContextCompat.getColor(context, R.color.tp_blue))
            }
            chipType == ThumbprintChipType.FILTER && isSelected -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.chip_filter_selected
                )
            }
            chipType == ThumbprintChipType.FILTER && !isSelected -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.chip_filter_unselected
                )
            }
        }
    }

    private fun applyTextStyling(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            textAttributes,
            NO_DEFAULT_STYLE_ATTRIBUTE,
            R.style.Thumbprint_CheckBox
        ).apply {
            try {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    getDimension(
                        textAttributes.indexOf(android.R.attr.textSize),
                        resources.getDimension(R.dimen.body_3)
                    )
                )
                setLineSpacing(
                    getDimension(
                        textAttributes.indexOf(android.R.attr.lineSpacingExtra),
                        resources.getDimension(R.dimen.body_3_line_spacing_extra)
                    ),
                    1f
                )
                typeface = getThumbprintFont(context, typeface, BOLD)
            } finally {
                recycle()
            }
        }
    }

    companion object {
        enum class ThumbprintChipType {
            TOGGLE, FILTER
        }

        private val textAttributes = arrayOf(
            android.R.attr.textSize,
            android.R.attr.lineSpacingExtra,
            android.R.attr.textColor
        ).toIntArray().sortedArray()

        private const val NO_DEFAULT_STYLE_ATTRIBUTE = 0
        private const val NO_DEFAULT_STYLE_RESOURCE = 0
    }
}
