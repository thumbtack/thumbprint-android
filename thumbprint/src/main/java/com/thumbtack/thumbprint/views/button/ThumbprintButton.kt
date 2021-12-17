package com.thumbtack.thumbprint.views.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface.BOLD
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.ThumbprintButtonAttributes
import com.thumbtack.thumbprint.WithDrawablesAttributes
import com.thumbtack.thumbprint.updateCharSequenceWithInlineDrawablesLimited
import com.thumbtack.thumbprint.updateTextWithTintedInlineDrawablesLimited
import com.thumbtack.thumbprint.utilities.getThumbprintFont
import com.thumbtack.thumbprint.views.button.ThumbprintButton.ThumbprintButtonType

private const val ANIMATION_DELAY_MS = 500L

/**
 * A Thumbprint-styled button, supporting the various [ThumbprintButtonType]s, loading, and bleed
 * states.
 *
 * [ThumbprintButton] supports both [WithDrawablesAttributes] and [ThumbprintButtonAttributes] to
 * configure the button in XML. Note that the default [ThumbprintButtonType] is
 * [ThumbprintButtonType.PRIMARY] unless it is specified via the `buttonType` attribute.
 *
 * See https://thumbprint.design/components/button/android/ for more design documentation.
 */
class ThumbprintButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatButton(ContextThemeWrapper(context, R.style.Thumbprint_Button), attrs) {

    enum class ThumbprintButtonType(
        val attributeValue: Int,
        val textColorStateListId: Int,
        val loadingAnimationColor: Int
    ) {
        PRIMARY(0, R.color.tp_button_primary_text_color_selector, R.color.tp_white),
        SECONDARY(1, R.color.tp_button_secondary_text_color_selector, R.color.tp_blue),
        TERTIARY(2, R.color.tp_button_tertiary_text_color_selector, R.color.tp_black_300),
        CAUTION(3, R.color.tp_button_caution_text_color_selector, R.color.tp_red),
        SOLID(4, R.color.tp_button_solid_text_color_selector, R.color.tp_black);
    }

    private val thumbprintButtonAttributes = ThumbprintButtonAttributes(context, attrs)
    private val withDrawableAttributes = WithDrawablesAttributes.readAttributes(context, attrs)

    private val loadingAnimationDrawable =
        AnimatedVectorDrawableCompat.create(context, R.drawable.loading_dots_animation)?.apply {
            registerAnimationCallback(
                object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        if (isLoading) {
                            postDelayed(
                                {
                                    if (!isRunning) start()
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                                        invalidateDrawable(this@apply)
                                    }
                                },
                                // Delay between each animation cycle. This is a workaround for
                                // adding an animation startOffset (which doesn't work with an
                                // objectAnimator set) in xml.
                                ANIMATION_DELAY_MS
                            )
                        }
                    }
                }
            )
        }

    private val drawablePadding = context.resources.getDimensionPixelSize(R.dimen.tp_space_1)

    /**
     * Store the [isEnabled] state separately, so that we can set it to false when [isLoading] is
     * true and restore to the previous value when [isLoading] is false.
     */
    private var internalIsEnabled = true

    var isBleed = thumbprintButtonAttributes.isBleed
        set(value) {
            field = value
            setButtonStyle()
        }

    var buttonType = thumbprintButtonAttributes.buttonType
        set(value) {
            field = value
            setButtonStyle()
        }

    // TODO(DS-941): Remove buttonText in favor of standard `android:text`.
    @Deprecated("Use `text` instead.")
    var buttonText: CharSequence? = thumbprintButtonAttributes.buttonText
        set(value) {
            field = value
            text = value
        }

    var isLoading = false
        set(value) {
            field = value
            if (value) {
                // Set the external enabled state, saving the current state in internalIsEnabled.
                super.setEnabled(false)
                loadingAnimationDrawable?.apply { if (!isRunning) start() }
            } else {
                loadingAnimationDrawable?.stop()
                // Restore the enabled state from internalIsEnabled.
                isEnabled = internalIsEnabled
            }
        }

    init {
        isEnabled = thumbprintButtonAttributes.isEnabled
        // If `app:buttonText` attribute is set in xml, that will be used. If not, then
        // `android:text` attribute will be used. Ideally, `android:text` should be preferred but
        // this is a temporary solution to support existing usages which already use `buttonText`
        if (!thumbprintButtonAttributes.buttonText.isNullOrEmpty()) {
            text = thumbprintButtonAttributes.buttonText
        }
        // This must be set in init to trigger the property setter. Property setter isn't invoked
        // on construction.
        isLoading = thumbprintButtonAttributes.isLoading

        // Note: Need to set these style attributes here because, for some reason, they aren't
        // picked up from the style resource when used with ContextThemeWrapper.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            stateListAnimator = null // Get rid of shadow effects.
        }
        TextViewCompat.setTextAppearance(this, R.style.Body2Bold)
        typeface = getThumbprintFont(context, typeface, BOLD)

        setButtonStyle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        loadingAnimationDrawable?.apply {
            setBounds(
                (width - intrinsicWidth) / 2,
                (height - intrinsicHeight) / 2,
                (width + intrinsicWidth) / 2,
                (height + intrinsicHeight) / 2
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isLoading) {
            loadingAnimationDrawable?.apply {
                draw(canvas)
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) invalidateDrawable(this)
            }
        } else {
            super.onDraw(canvas)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        internalIsEnabled = enabled
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(
            updateCharSequenceWithInlineDrawablesLimited(
                withDrawableAttributes,
                text,
                drawablePadding
            ),
            type
        )
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        updateTextWithTintedInlineDrawablesLimited(withDrawableAttributes)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return (who == loadingAnimationDrawable) || super.verifyDrawable(who)
    }

    private fun setButtonStyle() {
        val backgroundId = when (buttonType) {
            ThumbprintButtonType.PRIMARY -> if (isBleed) {
                R.drawable.button_primary_bleed_background
            } else {
                R.drawable.button_primary_background
            }
            ThumbprintButtonType.SECONDARY -> if (isBleed) {
                R.drawable.button_secondary_bleed_background
            } else {
                R.drawable.button_secondary_background
            }
            ThumbprintButtonType.TERTIARY -> if (isBleed) {
                R.drawable.button_tertiary_bleed_background
            } else {
                R.drawable.button_tertiary_background
            }
            ThumbprintButtonType.CAUTION -> if (isBleed) {
                R.drawable.button_caution_bleed_background
            } else {
                R.drawable.button_caution_background
            }
            ThumbprintButtonType.SOLID -> if (isBleed) {
                R.drawable.button_solid_bleed_background
            } else {
                R.drawable.button_solid_background
            }
        }
        setBackgroundResource(backgroundId)

        val loadingAnimationColor =
            ContextCompat.getColor(context, buttonType.loadingAnimationColor)
        loadingAnimationDrawable?.colorFilter =
            PorterDuffColorFilter(loadingAnimationColor, PorterDuff.Mode.SRC_ATOP)

        setTextColor(ContextCompat.getColorStateList(context, buttonType.textColorStateListId))
    }
}
