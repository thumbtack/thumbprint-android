package com.thumbtack.thumbprint.views.radiobutton

import android.content.Context
import android.util.AttributeSet
import com.thumbtack.thumbprint.views.compoundbutton.ThumbprintCompoundButtonLayoutDecorator

class ThumbprintContainerRadioButton(context: Context, attrs: AttributeSet? = null) :
    ThumbprintCompoundButtonLayoutDecorator(context, attrs, ThumbprintRadioButton(context, attrs))
