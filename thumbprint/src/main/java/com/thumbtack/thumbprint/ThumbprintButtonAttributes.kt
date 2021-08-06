package com.thumbtack.thumbprint

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.thumbtack.thumbprint.views.button.ThumbprintButton

/**
 * Wraps [R.styleable.ThumbprintButtonStyleable], parsing [attrs] into the class fields.
 */
class ThumbprintButtonAttributes(context: Context, attrs: AttributeSet?) {
    val buttonType: ThumbprintButton.ThumbprintButtonType
    val buttonText: CharSequence?
    val isEnabled: Boolean
    val isLoading: Boolean
    val isBleed: Boolean

    init {
        val styledAttributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ThumbprintButtonStyleable)

        buttonType = styledAttributes
            .getInt(
                R.styleable.ThumbprintButtonStyleable_buttonType,
                ThumbprintButton.ThumbprintButtonType.PRIMARY.attributeValue
            )
            .let {
                requireNotNull(
                    ThumbprintButton.ThumbprintButtonType.values()
                        .find { type -> type.attributeValue == it }
                ) { "unexpected buttonType value: $it" }
            }
        buttonText = styledAttributes.getText(R.styleable.ThumbprintButtonStyleable_buttonText)
        isEnabled = styledAttributes.getBoolean(
            R.styleable.ThumbprintButtonStyleable_isEnabled,
            true
        )
        isLoading = styledAttributes.getBoolean(
            R.styleable.ThumbprintButtonStyleable_isLoading,
            false
        )
        isBleed = styledAttributes.getBoolean(R.styleable.ThumbprintButtonStyleable_isBleed, false)

        styledAttributes.recycle()
    }
}
