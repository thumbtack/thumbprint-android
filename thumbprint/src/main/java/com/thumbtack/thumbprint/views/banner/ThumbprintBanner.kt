package com.thumbtack.thumbprint.views.banner

import android.content.Context
import android.content.res.TypedArray
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.thumbtack.thumbprint.BoldColoredLinkSpan
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.utilities.getThumbprintIcon
import com.thumbtack.thumbprint.withSpan
import kotlinx.android.synthetic.main.thumbprint_banner.view.*

class ThumbprintBanner(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private var typedArray: TypedArray = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.ThumbprintBannerStyleable,
        NO_DEFAULT_STYLE_ATTRIBUTE,
        NO_DEFAULT_STYLE_RESOURCE
    )

    var linkClickListener: (() -> Unit)? = null

    var text: CharSequence? = null
        set(value) {
            field = value
            buildText(value, linkText)
        }

    var linkText: CharSequence? = null
        set(value) {
            field = value
            buildText(text, value)
        }

    var bannerTheme: ThumbprintBannerType = ThumbprintBannerType.INFO
        set(value) {
            field = value
            bannerBackground.background = ContextCompat.getDrawable(
                context,
                getBackgroundColor(value)
            )
            bannerIcon.setColorFilter(getTextAndIconColor(value))
            buildText(text, linkText)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.thumbprint_banner, this, true)
        bannerIcon.setImageDrawable(
            getThumbprintIcon(
                context = context,
                defaultIcon = ContextCompat.getDrawable(context, R.drawable.default_info),
                iconRef = R.attr.infoFilledIcon
            )
        )

        bannerText.movementMethod = LinkMovementMethod()

        ViewCompat.enableAccessibleClickableSpanSupport(bannerText)

        typedArray.run {
            try {
                bannerTheme = when (getString(R.styleable.ThumbprintBannerStyleable_bannerTheme)) {
                    ThumbprintBannerType.WARNING.value -> ThumbprintBannerType.WARNING
                    ThumbprintBannerType.CAUTION.value -> ThumbprintBannerType.CAUTION
                    else -> ThumbprintBannerType.INFO
                }
                text = getString(R.styleable.ThumbprintBannerStyleable_android_text)
                linkText = getString(R.styleable.ThumbprintBannerStyleable_linkText)
            } finally {
                recycle()
            }
        }
    }

    private fun addBoldColoredLinkSpan(
        context: Context,
        text: CharSequence,
        stringBuilder: SpannableStringBuilder
    ) {
        stringBuilder.withSpan(
            BoldColoredLinkSpan(
                context = context,
                onClick = { linkClickListener?.invoke() },
                color = getTextAndIconColor(bannerTheme)
            )
        ) { stringBuilder.append(text) }
    }

    private fun getBackgroundColor(type: ThumbprintBannerType): Int {
        return when (type) {
            ThumbprintBannerType.INFO -> R.color.tp_blue_100
            ThumbprintBannerType.CAUTION -> R.color.tp_yellow_100
            ThumbprintBannerType.WARNING -> R.color.tp_red_100
        }
    }

    private fun getTextAndIconColor(type: ThumbprintBannerType): Int {
        val res = when (type) {
            ThumbprintBannerType.INFO -> R.color.tp_blue_600
            ThumbprintBannerType.CAUTION -> R.color.tp_yellow_600
            ThumbprintBannerType.WARNING -> R.color.tp_red_600
        }
        return ContextCompat.getColor(context, res)
    }

    private fun buildText(text: CharSequence?, linkText: CharSequence?) {
        bannerText.setTextColor(getTextAndIconColor(bannerTheme))
        val stringBuilder = SpannableStringBuilder()
        text?.let {
            stringBuilder.append(it)
        }
        linkText?.let {
            stringBuilder.append(" ")
            addBoldColoredLinkSpan(context, it, stringBuilder)
        }
        bannerText.text = stringBuilder
    }

    companion object {
        const val NO_DEFAULT_STYLE_ATTRIBUTE = 0
        const val NO_DEFAULT_STYLE_RESOURCE = 0
    }
}

enum class ThumbprintBannerType(val value: String) {
    INFO("info"), WARNING("warning"), CAUTION("caution");
}
