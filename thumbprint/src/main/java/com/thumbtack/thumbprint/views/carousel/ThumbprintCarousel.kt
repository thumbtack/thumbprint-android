package com.thumbtack.thumbprint.views.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.SpaceDecoration

/**
 * A [RecyclerView] which imposes standard Thumbprint styles for carousels; in particular:
 * - sets the orientation to horizontal
 * - uses a [androidx.recyclerview.widget.LinearLayoutManager]
 * - disables clipping for children within its padding (android:clipToPadding)
 * - adds a [R.dimen.tp_space_2] space between items
 *
 * Note that [ThumbprintCarousel] automatically applies [R.style.Thumbprint_Carousel]; it does not
 * need to be added as a style in XML.
 */
class ThumbprintCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(ContextThemeWrapper(context, R.style.Thumbprint_Carousel), attrs, defStyle) {
    init {
        // Warning: adding the LinearLayoutManager to the style causes a crash on Android 7 and
        // below. Test changes thoroughly on multiple Android versions.
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        addItemDecoration(
            SpaceDecoration(
                context = context,
                orientation = HORIZONTAL,
                dividerSizeRes = R.dimen.tp_space_2
            )
        )
    }
}
