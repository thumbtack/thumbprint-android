package com.thumbtack.thumbprint.views.compoundbutton

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.views.checkbox.ThumbprintCheckBox
import com.thumbtack.thumbprint.views.radiobutton.ThumbprintRadioButton

/**
 * A horizontal [LinearLayout] that adds the provided [compoundButton] as the first child in
 * that layout. It's used to decorate an arbitrary [View] or collection of [View]s with a
 * [CompoundButton]. Additionally, this class adds accessibility support, making this
 * the target of screen readers and the like and reading the contents appropriately.
 */
abstract class ThumbprintCompoundButtonLayoutDecorator(
    context: Context,
    attrs: AttributeSet? = null,
    private val compoundButton: CompoundButton
) : LinearLayout(context, attrs) {

    private val buttonLayoutWidth: Int
    private val buttonLayoutHeight: Int

    init {
        orientation = HORIZONTAL
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        isClickable = true
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ThumbprintContainerCompoundButtonStyleable,
            ThumbprintRadioButton.NO_DEFAULT_STYLE_ATTRIBUTE,
            ThumbprintRadioButton.NO_DEFAULT_STYLE_RESOURCE
        ).apply {
            try {
                buttonLayoutWidth = getLayoutDimension(
                    R.styleable.ThumbprintContainerCompoundButtonStyleable_button_layout_width,
                    WRAP_CONTENT
                )
                buttonLayoutHeight = getLayoutDimension(
                    R.styleable.ThumbprintContainerCompoundButtonStyleable_button_layout_height,
                    WRAP_CONTENT
                )
            } finally {
                recycle()
            }
        }
    }

    /**
     * Sets or indicates whether the [CompoundButton] is checked or not.
     */
    var isChecked: Boolean = false
        set(value) {
            field = value
            compoundButton.isChecked = value
        }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(compoundButton, 0)
        compoundButton.layoutParams.height = buttonLayoutHeight
        compoundButton.layoutParams.width = buttonLayoutWidth
        compoundButton.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        invalidate()
        requestLayout()
    }

    override fun performClick(): Boolean {
        val result = super.performClick()
        compoundButton.performClick()
        return result
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        val sb = StringBuilder()
        if (compoundButton is ThumbprintCheckBox) {
            sb.append(
                if (compoundButton.isIndeterminate) {
                    resources.getString(R.string.checkbox_indeterminate_utterance)
                } else if (compoundButton.isChecked) {
                    resources.getString(R.string.compound_button_checked_utterance)
                } else {
                    resources.getString(R.string.compound_button_unchecked_utterance)
                }
            )
            if (compoundButton.isError) {
                sb.append(resources.getString(R.string.compound_button_error_utterance))
            }
        } else if (compoundButton is ThumbprintRadioButton) {
            sb.append(
                if (compoundButton.isChecked) {
                    resources.getString(R.string.compound_button_checked_utterance)
                } else {
                    resources.getString(R.string.compound_button_unchecked_utterance)
                }
            )
            if (compoundButton.isError) {
                sb.append(resources.getString(R.string.compound_button_error_utterance))
            }
        } else {
            sb.append(
                if (compoundButton.isChecked) {
                    resources.getString(R.string.compound_button_checked_utterance)
                } else {
                    resources.getString(R.string.compound_button_unchecked_utterance)
                }
            )
        }

        for (index in 0 until childCount) {
            val childView = getChildAt(index)
            if (childView is TextView) {
                sb.append(childView.text)
            } else if (childView.contentDescription?.isNotBlank() == true) {
                sb.append(childView.contentDescription)
            }
        }
        info?.apply {
            this.contentDescription = sb.toString()
        }
    }

    override fun onPopulateAccessibilityEvent(event: AccessibilityEvent?) {
        super.onPopulateAccessibilityEvent(event)
        if (event?.eventType == TYPE_VIEW_CLICKED) {
            if (compoundButton.isChecked) {
                announceForAccessibility(
                    resources.getString(R.string.compound_button_unchecked_utterance)
                )
            } else {
                announceForAccessibility(
                    resources.getString(R.string.compound_button_checked_utterance)
                )
            }
        }
    }
}
