package com.thumbtack.thumbprint.views.edittext

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.thumbtack.thumbprint.R

/**
 * An EditText that manages its own state and conforms to the Thumbprint styles for each possible
 * state.
 * State transitions happen when the EditText's focus is changed, when text is changed (either
 * by key events or by calling [.setText] programatically, and by setting [hasError] or
 * [isDisabled].
 * A ThumbprintEditText instance cannot have both [hasError] and [isDisabled] set to [true] at the
 * same time: setting one to [true] will set the other the [false].
 */
abstract class ThumbprintEditTextBase(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    enum class STATE {
        DEFAULT, // aka UNSELECTED_AND_EMPTY
        DISABLED,
        ERROR,
        SELECTED_AND_EMPTY,
        SELECTED_AND_FILLED,
        UNSELECTED_AND_FILLED
    }

    private var errorValidator: ((CharSequence?) -> Boolean)? = null

    var hasError: Boolean = false
        set(value) {
            if (value && this.isDisabled) {
                isDisabled = false
            }
            field = value
            updateAndApplyState()
        }

    var isDisabled: Boolean = false
        set(value) {
            if (value && this.hasError) {
                hasError = false
            }
            field = value
            isEnabled = !value
            updateAndApplyState()
        }

    var state: STATE = STATE.DEFAULT
        private set

    init {
        addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (errorValidator != null) {
                        hasError = errorValidator?.invoke(s) == true
                    }
                    updateAndApplyState()
                }
            }
        )
    }

    private fun updateAndApplyState() {
        val focused = isFocused
        val hasInput = !text.isNullOrEmpty()

        when {
            hasError -> applyState(STATE.ERROR)
            isDisabled -> applyState(STATE.DISABLED)
            else -> {
                if (focused) {
                    if (hasInput && state != STATE.SELECTED_AND_FILLED) {
                        applyState(STATE.SELECTED_AND_FILLED)
                    } else if (!hasInput && state != STATE.SELECTED_AND_EMPTY) {
                        applyState(STATE.SELECTED_AND_EMPTY)
                    }
                } else {
                    if (hasInput && state != STATE.UNSELECTED_AND_FILLED) {
                        applyState(STATE.UNSELECTED_AND_FILLED)
                    } else if (!hasInput && state != STATE.DEFAULT) {
                        applyState(STATE.DEFAULT)
                    }
                }
            }
        }
    }

    private fun applyState(newState: STATE) {
        when (newState) {
            STATE.DEFAULT -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.edit_text_border_default
                )
                setTextColor(ContextCompat.getColor(context, R.color.black_300))
                setHintTextColor(ContextCompat.getColor(context, R.color.black_300))
            }

            STATE.DISABLED -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.edit_text_border_disabled
                )
                setTextColor(ContextCompat.getColor(context, R.color.gray))
                setHintTextColor(ContextCompat.getColor(context, R.color.gray))
            }

            STATE.ERROR -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.edit_text_border_error
                )
                setTextColor(ContextCompat.getColor(context, R.color.red))
                setHintTextColor(ContextCompat.getColor(context, R.color.red))
            }

            STATE.SELECTED_AND_EMPTY -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.edit_text_border_selected
                )
                setHintTextColor(ContextCompat.getColor(context, R.color.black_300))
            }

            STATE.SELECTED_AND_FILLED -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.edit_text_border_selected
                )
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            STATE.UNSELECTED_AND_FILLED -> {
                background = ContextCompat.getDrawable(
                    context,
                    R.drawable.edit_text_border_default
                )
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }

        state = newState
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        updateAndApplyState()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        updateAndApplyState()
    }

    fun hasErrorIf(condition: ((CharSequence?) -> Boolean)) {
        errorValidator = condition
    }

    fun isDisabledIf(condition: (() -> Boolean)) {
        isDisabled = condition()
    }
}
