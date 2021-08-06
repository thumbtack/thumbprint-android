package com.thumbtack.thumbprint.views.avatar

import android.content.Context
import android.util.AttributeSet
import com.squareup.picasso.Transformation
import com.thumbtack.thumbprint.CircularImageTransformation
import com.thumbtack.thumbprint.R

class ThumbprintUserAvatar(context: Context, attrs: AttributeSet? = null) :
    AvatarViewBase(context, attrs, false) {

    override fun getTransformation(): Transformation {
        return CircularImageTransformation
    }

    override fun getOnlineBadgeSize(size: Companion.Size): Int {
        return when (size) {
            Companion.Size.EXTRA_SMALL -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_extra_small)
            Companion.Size.SMALL -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_small)
            Companion.Size.MEDIUM -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_medium)
            Companion.Size.LARGE -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_large)
            Companion.Size.EXTRA_LARGE -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_extra_large)
        }
    }

    override fun getOnlineBadgeOffsetX(size: Companion.Size): Int {
        return when (size) {
            Companion.Size.EXTRA_SMALL -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_x_extra_small)
            Companion.Size.SMALL -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_x_small)
            Companion.Size.MEDIUM -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_x_medium)
            Companion.Size.LARGE -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_x_large)
            Companion.Size.EXTRA_LARGE -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_x_extra_large)
        }
    }

    override fun getOnlineBadgeOffsetY(size: Companion.Size): Int {
        return when (size) {
            Companion.Size.EXTRA_SMALL -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_y_extra_small)
            Companion.Size.SMALL -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_y_small)
            Companion.Size.MEDIUM -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_y_medium)
            Companion.Size.LARGE -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_y_large)
            Companion.Size.EXTRA_LARGE -> resources
                .getDimensionPixelSize(R.dimen.user_avatar_badge_offset_y_extra_large)
        }
    }
}
