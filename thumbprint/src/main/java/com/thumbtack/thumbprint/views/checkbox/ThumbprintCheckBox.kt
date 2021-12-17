package com.thumbtack.thumbprint.views.checkbox

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.utilities.getThumbprintFont

/**
 * Simple Thumbprint CheckBox--just checkbox and text--that supports error-state styling as well
 * as a "indeterminate" state.
 */
class ThumbprintCheckBox(context: Context, attrs: AttributeSet? = null) :
    AppCompatCheckBox(context, attrs) {

    private val defaultTextColors: ColorStateList? = textColors
    private val errorTextColor: Int

    init {
        // Stash the text color for the error attribute.
        context.theme.obtainStyledAttributes(
            R.style.Thumbprint_CheckBox_Error,
            errorStyleAttributes
        ).apply {
            try {
                errorTextColor = getColor(
                    errorStyleAttributes.indexOf(android.R.attr.textColor),
                    ContextCompat.getColor(context, R.color.tp_red)
                )
            } finally {
                recycle()
            }
        }
        // Set the button drawable to our custom one, to handle partial and error states
        buttonDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.thumbprint_simple_checkbox,
            null
        )

        applyCustomAttributes(context, attrs)
        applyTextStyling(context, attrs)
    }

    /**
     * Indicates whether the button should be rendered as being in an error state. The button
     * will still be enabled, and can therefore be checked/unchecked/partial, but its appearance
     * will indicate that there is an error condition, according to the standard Thumbprint
     * design. If the button is also disabled, then the styling for the disabled state will
     * take precedence.
     */
    var isError: Boolean = false
        set(value) {
            field = value
            updateForError(value)
            refreshDrawableState()
        }

    /**
     * Sets or unsets the indeterminate state for this button. Setting it (to "true") will
     * set this button's [isChecked] state to false. Setting it to false means that
     * [isIndeterminate] == false and [isChecked] == false.
     */
    var isIndeterminate: Boolean = false
        set(value) {
            field = value
            if (value) {
                isChecked = false
            }
            refreshDrawableState()
        }

    /**
     * Sets the checked state for this button. Setting it (to "true") will set the button's
     * [isIndeterminate] state to false. Setting it to false means that
     * [isIndeterminate] == false and [isChecked] == false.
     * @see [AppCompatCheckBox.setChecked].
     */
    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if (checked) {
            isIndeterminate = false
        }
    }

    /**
     * Enables or disables this check box. This button will render using the disabled rendering
     * style even if [isError] == true.
     * @see [AppCompatCheckBox.setEnabled].
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateForError(isError)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 2)
        if (isIndeterminate) {
            mergeDrawableStates(drawableState, indeterminateState)
        }
        if (isError) {
            View.mergeDrawableStates(drawableState, errorState)
        }
        return drawableState
    }

    /**
     * This decorates the [AccessibilityNodeInfo.setContentDescription] with utterances about the
     * error and indeterminate states of this checkbox, so that things like screen readers will
     * announce those states.
     */
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        info?.apply {
            val decoratedContentDescription = StringBuilder()
            if (isError) {
                decoratedContentDescription.append(
                    context.getString(R.string.compound_button_error_utterance)
                )
            }
            if (isIndeterminate) {
                decoratedContentDescription.append(
                    context.getString(R.string.checkbox_indeterminate_utterance)
                )
            }
            if (decoratedContentDescription.isNotEmpty()) {
                this.contentDescription = decoratedContentDescription
                    .append(
                        contentDescription.takeUnless {
                            it.isNullOrBlank()
                        } ?: text
                    )
                    .toString()
            }
        }
    }

    /**
     * Applies attributes specific to [ThumbprintCheckBox]: isError and isIndeterminate
     */
    private fun applyCustomAttributes(context: Context, attrs: AttributeSet?) {
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
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ThumbprintCheckBoxStyleable,
            NO_DEFAULT_STYLE_ATTRIBUTE,
            NO_DEFAULT_STYLE_RESOURCE
        ).apply {
            try {
                isIndeterminate = getBoolean(
                    R.styleable.ThumbprintCheckBoxStyleable_isIndeterminate,
                    false
                )
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
            R.style.Thumbprint_CheckBox
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
                compoundDrawablePadding = DRAWABLE_PADDING_PX
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
            setTextColor(errorTextColor)
        } else {
            if (defaultTextColors != null) {
                setTextColor(defaultTextColors)
            }
        }
    }

    companion object {
        private val errorStyleAttributes =
            arrayOf(android.R.attr.textColor, android.R.attr.buttonTint).toIntArray().sortedArray()

        private val textAttributes = arrayOf(
            android.R.attr.textSize,
            android.R.attr.lineSpacingExtra,
            android.R.attr.textColor
        ).toIntArray().sortedArray()

        private val indeterminateState = intArrayOf(R.attr.state_indeterminate)
        private val errorState = intArrayOf(R.attr.state_error)

        const val NO_DEFAULT_STYLE_ATTRIBUTE = 0
        const val NO_DEFAULT_STYLE_RESOURCE = 0

        private const val DRAWABLE_PADDING_PX = 60
    }
}
