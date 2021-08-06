package com.thumbtack.thumbprint

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

class AlignedImageSpan(
    drawable: Drawable,
    verticalAlignment: Int = ALIGN_CENTER,
    var paddingLeft: Int = 0,
    var paddingTop: Int = 0,
    var paddingRight: Int = 0,
    var paddingBottom: Int = 0
) : ImageSpan(drawable, verticalAlignment) {

    private var drawableRef: WeakReference<Drawable>? = null

    private val cachedDrawable: Drawable
        get() = drawableRef?.get() ?: drawable.apply {
            // Set bounds so that vector drawables know what size to be
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            drawableRef = WeakReference(this)
        }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val pfm = paint.fontMetrics
        fm?.apply {
            ascent = pfm.ascent.roundToInt() - paddingTop
            descent = pfm.descent.roundToInt() + paddingBottom
            top = pfm.top.roundToInt() - paddingTop
            bottom = pfm.bottom.roundToInt() + paddingBottom
        }

        return cachedDrawable.intrinsicWidth + paddingLeft + paddingRight
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val fm = paint.fontMetrics
        val drawableHeight = cachedDrawable.intrinsicHeight
        val textVerticalCenter = y + (fm.ascent + fm.descent) / 2f

        canvas.save()

        val dy = when (verticalAlignment) {
            // Bottom of the image should be aligned to the bottom of the line
            ALIGN_BOTTOM -> (bottom - drawableHeight).toFloat()
            // Bottom of the image should be aligned to the baseline of the text
            ALIGN_BASELINE -> (y - drawableHeight).toFloat()
            // Center of the view
            ALIGN_CENTER -> (bottom - top - drawableHeight) / 2f
            // Center of the image should be aligned to the center of the text i.e. the mid-point
            // between the baseline + ascent and baseline + descent, i.e. the top of the image
            // should be the mid-point - half of the image's height.
            ALIGN_TEXT_CENTER -> textVerticalCenter - (drawableHeight / 2f)
            // Aligned to the top
            ALIGN_TOP -> 0f
            else -> error("Unrecognized vertical alignment: $verticalAlignment")
        }

        canvas.translate(x + paddingLeft, dy)
        cachedDrawable.draw(canvas)

        canvas.restore()
    }

    companion object {
        const val ALIGN_CENTER: Int = 2
        const val ALIGN_TEXT_CENTER: Int = 3
        const val ALIGN_TOP: Int = 4
    }
}
