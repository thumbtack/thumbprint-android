package com.thumbtack.thumbprint.widget

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.thumbtack.thumbprint.R
import com.thumbtack.thumbprint.TextOrResourceId

/**
 * A Thumbprint widget for showing a short message briefly at the base of the screen, with
 * an optional action which can be triggered by pressing a textual anchor.
 *
 * The toast auto-hides after a short time (2-3 seconds).
 *
 * [ThumbprintToast] is constructed using a builder pattern.`
 * ThumbprintToast.createWithContainer(toastContainer)
 *     .withMessage('fire!')
 *     .show()
 *
 * Example with an action:
 * ThumbprintToast.createWithContainer(toastContainer)
 *     .withMessage('fire!')
 *     .withAction('extinguish!') {
 *        // ...
 *     }
 *     .show()
 *  `
 */
class ThumbprintToast {

    private var msg: TextOrResourceId = TextOrResourceId.create("")
    private var container: View? = null
    private var actionText: TextOrResourceId? = null
    private var backgroundColor: Int? = null
    private var actionListener: View.OnClickListener = View.OnClickListener {}
    private var toastLength = Snackbar.LENGTH_SHORT
    private var callbackList: MutableList<BaseTransientBottomBar.BaseCallback<Snackbar>> =
        mutableListOf()

    /** Use the given string message for the toast */
    fun withMessage(msg: String): ThumbprintToast {
        this.msg = TextOrResourceId.create(msg)
        return this
    }

    /** Use the given string resource for the toast message */
    fun withMessage(msg: Int): ThumbprintToast {
        this.msg = TextOrResourceId.create(msg)
        return this
    }

    /**
     * Show the toast at the base of the given container. This is normally a
     * [CoordinatorLayout]
     */
    fun withContainer(container: View?): ThumbprintToast {
        this.container = container
        return this
    }

    /**
     * Offer an action in the toast by pressing on the given text.
     */
    fun withAction(textResourceId: Int, actionListener: View.OnClickListener): ThumbprintToast {
        return withAction(
            TextOrResourceId.create(textResourceId),
            actionListener
        )
    }

    /**
     * Offer an action in the toast by pressing on the given text.
     */
    fun withAction(textResourceId: Int, actionListener: () -> Unit): ThumbprintToast {
        return withAction(
            TextOrResourceId.create(textResourceId),
            actionListener
        )
    }

    /**
     * Offer an action in the toast by pressing on the given text.
     */
    fun withAction(text: String, actionListener: () -> Unit): ThumbprintToast {
        return withAction(
            TextOrResourceId.create(text),
            actionListener
        )
    }

    /**
     * Set the background color of the toast (default is black).
     */
    fun withBackgroundColor(@ColorInt color: Int): ThumbprintToast {
        this.backgroundColor = color
        return this
    }

    /**
     * Sets the toast duration to be a long period of time
     */
    fun withLongDuration(): ThumbprintToast {
        this.toastLength = Snackbar.LENGTH_LONG
        return this
    }

    /**
     * Sets the toast to be permanent
     */
    fun withPermanentDuration(): ThumbprintToast {
        this.toastLength = Snackbar.LENGTH_INDEFINITE
        return this
    }

    /**
     * Adds a callback to the toast. This can be called multiple times to add multiple callbacks.
     */
    fun withCallback(callback: BaseTransientBottomBar.BaseCallback<Snackbar>): ThumbprintToast {
        callbackList.add(callback)
        return this
    }

    private fun withAction(
        textOrResourceId: TextOrResourceId,
        actionListener: () -> Unit
    ): ThumbprintToast {
        return withAction(
            textOrResourceId,
            View.OnClickListener { actionListener.invoke() }
        )
    }

    private fun withAction(
        textOrResourceId: TextOrResourceId,
        actionListener: View.OnClickListener
    ): ThumbprintToast {
        this.actionText = textOrResourceId
        this.actionListener = actionListener
        return this
    }

    /**
     * Show the toast.
     */
    fun show() {
        container?.let { toastContainer ->
            val context = toastContainer.context

            val snackbar = Snackbar.make(toastContainer, msg.toString(context), toastLength)
            callbackList.forEach { snackbar.addCallback(it) }
            decorate(snackbar, context)
            setTextWithStyle(snackbar, context)
            snackbar.show()
        }
    }

    private fun decorate(snackbar: Snackbar, context: Context) {
        snackbar.view.background = ContextCompat.getDrawable(context, R.drawable.toast_background)
        backgroundColor?.let {
            snackbar.view.setBackgroundColor(it)
        }
    }

    private fun setTextWithStyle(snackbar: Snackbar, context: Context) {
        val snackbarTextView = snackbar.view.findViewById<TextView>(R.id.snackbar_text)
        snackbarTextView?.let {
            TextViewCompat.setTextAppearance(it, R.style.Body1Regular)
        }
        actionText?.let {
            snackbar.setAction(it.toString(context), actionListener)
            val snackbarActionTextView =
                snackbar.view.findViewById<TextView>(R.id.snackbar_action)
            snackbarActionTextView?.let {
                it.isAllCaps = false
                TextViewCompat.setTextAppearance(it, R.style.Body1Regular)
            }
            snackbar.setActionTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )
        }
    }

    companion object {
        /** Create a new Toast inside the given container */
        fun createWithContainer(container: View) = ThumbprintToast().withContainer(container)
    }
}
