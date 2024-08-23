package com.thumbtack.thumbprint

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface.BOLD
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.thumbtack.thumbprint.utilities.getThumbprintFont
import kotlin.math.min

/**
 * A placeholder [Drawable] for a blank [com.thumbtack.thumbprint.views.AvatarView]; that is, one
 * which has no profile image, is temporarily using a placeholder, or encountered an error loading
 * the image.
 *
 * Note that while Picasso applies a circle/rounded-rectangle transformation to the loaded image,
 * the transformation is not applied to this [Drawable], so it must manually fit its background to
 * a circle/rounded-rectangle in [draw].
 *
 * The background color is determined within this Thumbprint component, based on the ASCII values of
 * the initials' first character; this implementation is shared between Thumbprint platforms (i.e.
 * web and iOS), see [setColorsFromInitials].
 */
internal class BlankAvatarDrawable(
    val context: Context,
    private val initialsFontSize: Int
) : Drawable() {

    var text: String? = null
        set(value) {
            field = value
            setColorsFromInitials(value)
            invalidateSelf()
        }

    private val textColors = context.resources.getIntArray(R.array.blank_avatar_text_colors)
    private val backgroundColors = context.resources.getIntArray(
        R.array.blank_avatar_background_colors
    )

    private var backgroundColor: Int = -1
    private var textColor: Int = -1
    private val paint: Paint = Paint().apply {
        textSize = initialsFontSize.toFloat()

        typeface = getThumbprintFont(context, typeface, BOLD)

        isAntiAlias = true
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    private var bounds: RectF = RectF()
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var textY: Float = 0f
    private var radius: Float = 0f

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        this.bounds = RectF(bounds).also {
            centerX = it.centerX()
            centerY = it.centerY()
            radius = min(it.width(), it.height()) / 2f
            textY = centerY - (paint.ascent() / 2f)
        }
    }

    override fun draw(canvas: Canvas) {
        paint.color = backgroundColor
        canvas.drawCircle(centerX, centerY, radius, paint)

        text?.let {
            paint.color = textColor
            canvas.drawText(it, centerX, textY, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    private fun setColorsFromInitials(initials: String?) {
        val hash = initials?.firstOrNull()?.code
        if (hash == null) {
            backgroundColor = ContextCompat.getColor(context, R.color.tp_gray_200)
            textColor = ContextCompat.getColor(context, R.color.tp_black)
        } else {
            backgroundColor = backgroundColors[hash % backgroundColors.size]
            textColor = textColors[hash % textColors.size]
        }
    }
}
