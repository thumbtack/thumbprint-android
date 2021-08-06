package com.thumbtack.thumbprint.views.checkbox

import android.content.Context
import android.util.AttributeSet
import com.thumbtack.thumbprint.views.compoundbutton.ThumbprintCompoundButtonLayoutDecorator

class ThumbprintContainerCheckBox(context: Context, attrs: AttributeSet? = null) :
    ThumbprintCompoundButtonLayoutDecorator(context, attrs, ThumbprintCheckBox(context, attrs))
