package com.thumbtack.thumbprint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.squareup.picasso.Transformation
import com.thumbtack.thumbprint.RoundedCornersImageTransformation.Companion.get
import kotlin.math.min

/**
 * A [Transformation] which rounds the image corners by Thumbprint's [R.dimen.round_corner_radius].
 *
 * For memory and GC efficiency, this class acts as a singleton object lazily created via [get].
 */
class RoundedCornersImageTransformation private constructor(private val radius: Float) :
    Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val config = Bitmap.Config.ARGB_8888
        val output = Bitmap.createBitmap(source.width, source.height, config)

        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        val rect = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        Canvas(output).drawRoundRect(rect, radius, radius, paint)

        if (source != output) {
            source.recycle()
        }
        return output
    }

    override fun key() = "rounded(radius=$radius)"

    companion object {

        private var transformation: RoundedCornersImageTransformation? = null

        fun get(context: Context): RoundedCornersImageTransformation {
            return transformation
                ?: RoundedCornersImageTransformation(
                    radius = context.resources
                        .getDimensionPixelSize(R.dimen.round_corner_radius)
                        .toFloat()
                ).also { transformation = it }
        }
    }
}

/**
 * A [Transformation] which crops the source image into a centered circle with radius equal to the
 * minimum of the source's width and height.
 */
object CircularImageTransformation : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val r = size / 2f

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        val config = source.config ?: Bitmap.Config.ARGB_8888
        val output = Bitmap.createBitmap(size, size, config)

        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        Canvas(output).drawCircle(r, r, r, paint)

        if (squaredBitmap != source) {
            source.recycle()
        }
        squaredBitmap.recycle()

        return output
    }

    override fun key() = "circle"
}
