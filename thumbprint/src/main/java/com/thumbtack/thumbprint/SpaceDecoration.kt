package com.thumbtack.thumbprint

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

/**
 * A [RecyclerView.ItemDecoration] which adds space between items in the [RecyclerView].
 *
 * Space is added before the first item iff [beforeFirst] (defaults to false).
 * Space is added after the last item iff [afterLast] (defaults to false).
 * Space is added between items i and i+1 iff filter(i, i+1) returns true (defaults to always return
 * true).
 */
open class SpaceDecoration(
    context: Context,
    @RecyclerView.Orientation protected val orientation: Int = RecyclerView.VERTICAL,
    protected val beforeFirst: Boolean = false,
    protected val afterLast: Boolean = false,
    protected val filter: (Int, Int) -> Boolean = { _, _ -> true },
    @DimenRes dividerSizeRes: Int = R.dimen.horizontal_divider_height
) : RecyclerView.ItemDecoration() {
    protected val dividerSize = context.resources.getDimensionPixelSize(dividerSizeRes)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }

        val isFirst = position == 0
        val isLast = position == (parent.adapter?.itemCount ?: 0) - 1

        if (isFirst && beforeFirst) {
            when (orientation) {
                RecyclerView.VERTICAL -> outRect.top = dividerSize
                RecyclerView.HORIZONTAL -> outRect.left = dividerSize
            }
        }

        if ((isLast && afterLast) || (!isLast && filter(position, position + 1))) {
            when (orientation) {
                RecyclerView.VERTICAL -> outRect.bottom = dividerSize
                RecyclerView.HORIZONTAL -> outRect.right = dividerSize
            }
        }
    }
}

class TwoColumnGridSpaceDecoration(
    context: Context,
    @DimenRes private val spaceSizeRes: Int
) : RecyclerView.ItemDecoration() {
    private val space = context.resources.getDimensionPixelSize(spaceSizeRes)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) <= 1) {
            outRect.top = space
        } else {
            outRect.top = 0
        }

        // Add right margin for left column and left margin for right column
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.right = space / 2
        } else {
            outRect.left = space / 2
        }
    }
}

class CustomSpaceDecoration(
    context: Context,
    @DimenRes topSpaceSizeRes: Int? = null,
    @DimenRes bottomSpaceSizeRes: Int? = null,
    @DimenRes leftSpaceSizeRes: Int? = null,
    @DimenRes rightSpaceSizeRes: Int? = null
) : RecyclerView.ItemDecoration() {
    private val topSpace = topSpaceSizeRes?.let { context.resources.getDimensionPixelSize(it) } ?: 0
    private val bottomSpace =
        bottomSpaceSizeRes?.let { context.resources.getDimensionPixelSize(it) } ?: 0
    private val leftSpace =
        leftSpaceSizeRes?.let { context.resources.getDimensionPixelSize(it) } ?: 0
    private val rightSpace =
        rightSpaceSizeRes?.let { context.resources.getDimensionPixelSize(it) } ?: 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = topSpace
        outRect.bottom = bottomSpace
        outRect.left = leftSpace
        outRect.right = rightSpace
    }
}
