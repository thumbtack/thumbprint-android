package com.thumbtack.thumbprint.views.fab

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.thumbtack.thumbprint.R

class ThumbprintIconFab constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageButton(context, attrs) {

    init {
        imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.tp_white))
        setBackgroundResource(R.drawable.icon_fab_background)
        elevation = resources.getDimension(R.dimen.shadow_400)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val iconSize = resources.getDimensionPixelSize(R.dimen.fab_icon_size)
        layoutParams.width = iconSize
        layoutParams.height = iconSize
    }
}
