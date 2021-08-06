package com.thumbtack.thumbprint.views.chip

import android.content.Context
import android.util.AttributeSet

class ThumbprintToggleChip(
    context: Context,
    attrs: AttributeSet? = null
) : ThumbprintChipBase(context, attrs, Companion.ThumbprintChipType.TOGGLE)

class ThumbprintFilterChip(
    context: Context,
    attrs: AttributeSet? = null
) : ThumbprintChipBase(context, attrs, Companion.ThumbprintChipType.FILTER)
