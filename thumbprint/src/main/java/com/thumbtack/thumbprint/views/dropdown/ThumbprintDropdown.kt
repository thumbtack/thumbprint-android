package com.thumbtack.thumbprint.views.dropdown

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.setVisibleIfTrue
import kotlinx.android.synthetic.main.thumbprint_dropdown.view.*
import kotlinx.android.synthetic.main.thumbprint_dropdown.view.errorText as errorTextView

/**
 * [ThumbprintSpinnerInternal] with a helper error text and Thumbprint styling.
 *
 * Configurable attributes: android:entries, android:isEnabled, app:state_error, app:errorText
 */
class ThumbprintDropdown(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var hasError: Boolean = false
        set(value) {
            field = value
            spinner.hasError = value
            updateErrorText()
        }

    var errorText: String? = null
        set(value) {
            field = value
            updateErrorText()
        }

    var entries: Array<CharSequence>? = null
        set(value) {
            field = value
            spinner.entries = value
        }

    @LayoutRes
    private val layoutRes = R.layout.thumbprint_dropdown

    private var typedArray: TypedArray = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.ThumbprintDropdown,
        NO_DEFAULT_STYLE_ATTRIBUTE,
        NO_DEFAULT_STYLE_RESOURCE
    )

    init {
        LayoutInflater.from(context).inflate(layoutRes, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Safer to set attributes of the child views here since they won't be null.
        typedArray.run {
            try {
                errorText = getString(R.styleable.ThumbprintDropdown_errorText)
                hasError = getBoolean(R.styleable.ThumbprintDropdown_state_error, false)
                entries = getTextArray(R.styleable.ThumbprintDropdown_android_entries)
            } finally {
                recycle()
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        spinner.isEnabled = enabled
        updateErrorText()
    }

    private fun updateErrorText() {
        errorTextView.text = errorText
        errorTextView.setVisibleIfTrue(hasError && isEnabled && errorText?.isNotBlank() == true)
    }

    companion object {

        const val NO_DEFAULT_STYLE_ATTRIBUTE = 0
        const val NO_DEFAULT_STYLE_RESOURCE = 0
    }
}
