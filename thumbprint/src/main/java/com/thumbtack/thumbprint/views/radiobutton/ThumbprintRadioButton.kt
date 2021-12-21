package com.thumbtack.thumbprint.views.radiobutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.accessibility.AccessibilityNodeInfo
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.utilities.getThumbprintFont

/**
 * Simple Thumbprint radio button--just button and text--that supports error-state styling.
 */
class ThumbprintRadioButton(context: Context, attrs: AttributeSet? = null) :
    AppCompatRadioButton(context, attrs) {

    private val defaultTextColors: ColorStateList? = textColors
    private val defaultButtonTintList: ColorStateList? = buttonTintList
    private val defaultButtonDrawable: Drawable? = CompoundButtonCompat.getButtonDrawable(this)
    private val errorButtonDrawable: Drawable?
    private val errorTextColor: Int

    init {
        // Stash the button drawable and text color for the error attribute.
        context.theme.obtainStyledAttributes(
            R.style.Thumbprint_RadioButton_Error,
            errorStyleAttributes
        ).apply {
            try {
                errorButtonDrawable =
                    defaultButtonDrawable?.constantState?.newDrawable()?.mutate()?.apply {
                        setTintList(
                            ColorStateList.valueOf(
                                getColor(
                                    errorStyleAttributes.indexOf(android.R.attr.buttonTint),
                                    ContextCompat.getColor(context, R.color.tp_red)
                                )
                            )
                        )
                    }
                errorTextColor = getColor(
                    errorStyleAttributes.indexOf(android.R.attr.textColor),
                    ContextCompat.getColor(context, R.color.tp_red)
                )
            } finally {
                recycle()
            }
        }
        // Apply the isError attribute if set
        applyIsErrorAttribute(context, attrs)
        // Apply text styling
        applyTextStyling(context, attrs)
    }

    /**
     * Indicates whether the button should be rendered as being in an error state. The button
     * will still be enabled, and can therefore be checked/unchecked, but its appearance
     * will indicate that there is an error condition, according to the standard Thumbprint
     * design. If the button is also disabled, then the styling for the disabled state will
     * take precedence.
     */
    var isError: Boolean = false
        set(value) {
            field = value
            updateForError(value)
        }

    /**
     * Enables or disables the radio button, overriding the [isError] setting as well.
     * @see [AppCompatRadioButton.setEnabled].
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateForError(isError)
    }

    /**
     * If [isError] is true, this will call [AccessibilityNodeInfo.setContentDescription]
     * with the phrase "Error. " + contentDescription|text to indicate that this
     * [ThumbprintRadioButton] is in an error state. That way, it will be made clear to
     * Accessibility Services like screen readers.
     */
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        info?.apply {
            if (isError) {
                this.contentDescription =
                    context.getString(R.string.compound_button_error_utterance)
                        .plus(
                            contentDescription.takeUnless {
                                it.isNullOrBlank()
                            } ?: text
                        )
            }
        }
    }

    private fun applyIsErrorAttribute(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ThumbprintCompoundButtonStyleable,
            NO_DEFAULT_STYLE_ATTRIBUTE,
            NO_DEFAULT_STYLE_RESOURCE
        ).apply {
            try {
                isError = getBoolean(R.styleable.ThumbprintCompoundButtonStyleable_isError, false)
            } finally {
                recycle()
            }
        }
    }

    private fun applyTextStyling(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            textAttributes,
            NO_DEFAULT_STYLE_ATTRIBUTE,
            R.style.Thumbprint_RadioButton
        ).apply {
            try {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    getDimension(
                        textAttributes.indexOf(android.R.attr.textSize),
                        resources.getDimension(R.dimen.body_1)
                    )
                )
                setLineSpacing(
                    getDimension(
                        textAttributes.indexOf(android.R.attr.lineSpacingExtra),
                        resources.getDimension(R.dimen.body_1_line_spacing_extra)
                    ),
                    1f
                )
                typeface = getThumbprintFont(context, typeface)
                if (isEnabled && !isError) {
                    setTextColor(
                        getColor(
                            textAttributes.indexOf(android.R.attr.textColor),
                            ContextCompat.getColor(context, R.color.tp_black)
                        )
                    )
                }
            } finally {
                recycle()
            }
        }
    }

    private fun updateForError(isError: Boolean) {
        if (isEnabled && isError) {
            buttonDrawable = errorButtonDrawable
            setTextColor(errorTextColor)
        } else {
            buttonDrawable = defaultButtonDrawable
            if (defaultTextColors != null) {
                setTextColor(defaultTextColors)
            }
            if (defaultButtonTintList != null) {
                buttonTintList = defaultButtonTintList
            }
        }
    }

    companion object {
        // Attribute arrays that are passed to obtainStyledAttributes() must be sorted,
        // per documentation for that method.

        private val errorStyleAttributes =
            arrayOf(android.R.attr.textColor, android.R.attr.buttonTint).toIntArray().sortedArray()

        private val textAttributes = arrayOf(
            android.R.attr.textSize,
            android.R.attr.lineSpacingExtra,
            android.R.attr.textColor
        ).toIntArray().sortedArray()

        const val NO_DEFAULT_STYLE_ATTRIBUTE = 0
        const val NO_DEFAULT_STYLE_RESOURCE = 0
    }
}
