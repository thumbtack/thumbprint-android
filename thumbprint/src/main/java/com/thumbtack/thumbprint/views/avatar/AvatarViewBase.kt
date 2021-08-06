package com.thumbtack.thumbprint.views.avatar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleableRes
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.thumbtack.thumbprint.BlankAvatarDrawable
import com.thumbtack.thumbprint.R
import kotlinx.android.synthetic.main.avatar_view.view.*
import java.util.Locale

/**
 * Displays a Thumbprinted avatar image for a user.
 *
 * Avatars may be entities ([isEntity]), i.e. a business rather than an individual, which use a
 * rounded-rectangle shape rather than a circle and a single initial rather than two.
 */
abstract class AvatarViewBase(context: Context, attrs: AttributeSet? = null, isEntity: Boolean) :
    RelativeLayout(context, attrs) {

    companion object {
        private const val DEFAULT_ATTRIBUTE_INDEX = -1

        enum class Size(val attributeName: String) {
            EXTRA_SMALL("extraSmall"),
            SMALL("small"),
            MEDIUM("medium"),
            LARGE("large"),
            EXTRA_LARGE("extraLarge")
        }
    }

    class InvalidSizeException(message: String) : Exception(message)

    private val fontSize: Int

    private val size: Size

    // Used either if we do not provide a URL or if the URL fails to load an image
    // resource
    private val initialsPlaceholder: BlankAvatarDrawable

    // Used to display a gray background while loading an image URL (if one is supplied)
    private val loadingPlaceholder: BlankAvatarDrawable

    @LayoutRes
    private val layoutRes = R.layout.avatar_view

    init {
        LayoutInflater.from(context).inflate(layoutRes, this, true)

        @StyleableRes val initialsFontSizeAttrIndex = 0
        @StyleableRes val sizeAttrIndex = 1
        val typedArray = context.obtainStyledAttributes(
            attrs,
            intArrayOf(
                R.attr.initials_font_size,
                R.attr.size
            )
        )

        fontSize = resources.getDimensionPixelSize(
            typedArray.getResourceId(initialsFontSizeAttrIndex, DEFAULT_ATTRIBUTE_INDEX)
        )
        val sizeAttr = typedArray.getString(sizeAttrIndex)
        typedArray.recycle()

        size = when (sizeAttr) {
            Size.EXTRA_SMALL.attributeName -> Size.EXTRA_SMALL
            Size.SMALL.attributeName -> Size.SMALL
            Size.MEDIUM.attributeName -> Size.MEDIUM
            Size.LARGE.attributeName -> Size.LARGE
            Size.EXTRA_LARGE.attributeName -> Size.EXTRA_LARGE
            else -> {
                throw InvalidSizeException("Must specify a size for avatar")
            }
        }

        initialsPlaceholder = BlankAvatarDrawable(
            context,
            isEntity = isEntity,
            initialsFontSize = fontSize
        )

        loadingPlaceholder = BlankAvatarDrawable(
            context,
            isEntity = isEntity,
            initialsFontSize = fontSize
        )
    }

    /**
     * Begins loading the image from the given [imageUrl], positions and sizes the online badge,
     * sets its visiblity based on [isOnline], and create a [BlankAvatarDrawable] as the
     * fallback avatar. The [BlankAvatarDrawable] will contain the string [initials] and use the
     * colors specified in [BlankAvatarDrawable.setColorsFromInitials], which maps the first letter
     * of [initials] to background and text color values.
     *
     * If no [imageUrl] is given, or if there is an error fetching the image, then the
     * [BlankAvatarDrawable] created from [initials] will be displayed. A gray placeholder is shown
     * during the image's loading if an [imageUrl] is specified.
     *
     * If both [imageUrl] and [initials] are unspecified, no avatar will be displayed.
     *
     * As specified in Thumbprint style guides, entity avatars should pass in one letter
     * for [initials] and user avatars should pass in two letters for [initials].
     */
    fun bind(imageUrl: String? = null, initials: String? = null, isOnline: Boolean = false) {
        this.sizeAndTranslateOnlineBadge(size)

        setIsOnline(isOnline)

        val imageSize = this.layoutParams.height // Width and height should be equal

        if (imageUrl.isNullOrBlank()) {
            Picasso.get().cancelRequest(avatar)
        }

        initialsPlaceholder.text = initials?.toUpperCase(Locale.ENGLISH)

        loadingPlaceholder.text = null // Makes a gray placeholder with no text

        Picasso.get()
            .load(imageUrl?.takeUnless { it.isNullOrBlank() })
            .resize(imageSize, imageSize)
            .centerCrop()
            .error(initialsPlaceholder)
            .also {
                // Only use initials as placeholder if we did not explicitly
                // define a URL. If fetching the URL fails,
                // the previous .error() call will show the initials placeholder
                // If we did define a URL, show a gray loading placeholder
                if (imageUrl == null) {
                    it.placeholder(initialsPlaceholder)
                } else {
                    it.placeholder(loadingPlaceholder)
                }
            }
            .transform(getTransformation())
            .into(avatar)
    }

    private fun sizeAndTranslateOnlineBadge(size: Size) {
        this.setWillNotDraw(true)

        val dpValue = getOnlineBadgeSize(size)
        val offsetX = getOnlineBadgeOffsetX(size)
        val offsetY = getOnlineBadgeOffsetY(size)

        badge.apply {
            layoutParams.height = dpValue
            layoutParams.width = dpValue
            translationX = offsetX.toFloat()
            translationY = offsetY.toFloat()
        }
    }

    fun setIsOnline(value: Boolean) {
        badge.visibility = if (value) View.VISIBLE else View.GONE
    }

    /**
     * Returns the transformation needed for the Image View,
     * which is either a circular mask for user avatars or a
     * rounded rectangle mask for entity avatars.
     */
    abstract fun getTransformation(): Transformation

    /**
     * Returns the size in pixels of the online badge
     */
    abstract fun getOnlineBadgeSize(size: Size): Int

    /**
     * Returns the X translation, in pixels, that the online badge
     * needs to move given the size of the avatar.
     *
     * Because Android clips children outside of a View Group by default, it is not
     * possible to translate the badge outside of the image (without setting every parent of the
     * image to not clip children). As a result, the position of badges on Android are
     * different than those on Web/iOS.
     */
    abstract fun getOnlineBadgeOffsetX(size: Size): Int

    /**
     * Returns the Y translation, in pixels, that the online badge
     * needs to move given the size of the avatar.
     *
     * Because Android clips children outside of a View Group by default, it is not
     * possible to translate the badge outside of the image (without setting every parent of the
     * image to not clip children). As a result, the position of badges on Android are
     * different than those on Web/iOS.
     */
    abstract fun getOnlineBadgeOffsetY(size: Size): Int
}
