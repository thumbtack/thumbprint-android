package com.thumbtack.thumbprint.views.dropdown

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.thumbtack.thumbprint.R

/**
 * A custom [AppCompatSpinner] with a [hasError] state, easy adapter entry handling, and the
 * ability to trigger [isSelected] state when the dropdown is open/closed.
 *
 * Note: This view is used as part of [ThumbprintDropdown]. It's recommended to use
 * [ThumbprintDropdown] instead of using this view directly.
 */
class ThumbprintSpinnerInternal(context: Context, attrs: AttributeSet) :
    AppCompatSpinner(context, attrs) {

    var hasError: Boolean = false
        set(value) {
            field = value

            // Updates the view's state so the proper background is set
            post { refreshDrawableState() }
        }

    var entries: Array<CharSequence>? = null
        set(value) {
            field = value
            updateAdapter(newEntries = true)
        }

    private var isDropdownOpen = false

    /**
     * Supplies the [entries] to the [adapter] with the proper [TextView] layout
     * based on the Spinner's current state.
     *
     * If we are re-using the same entries when updating the adapter such as when this function
     * is called after a state change, [newEntries] should be false and
     * will keep the position of its selection.
     */
    private fun updateAdapter(newEntries: Boolean) {
        entries?.let { entries ->
            val itemLayout = if (hasError && this.isEnabled) {
                R.layout.thumbprint_dropdown_item_error
            } else {
                R.layout.thumbprint_dropdown_item
            }

            ArrayAdapter(context, itemLayout, entries).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                val currentSelectedItemPosition = selectedItemPosition
                this.adapter = adapter

                if (!newEntries) {
                    // Set saved position after resetting the adapter if old entries are used
                    setSelection(currentSelectedItemPosition)
                }
            }
        }
    }

    /**
     * Adds handling for custom states
     */
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)

        if (hasError) {
            View.mergeDrawableStates(drawableState, STATE_ERROR)
        }

        /**
         * Update TextView colors when Spinner's state changes.
         * Note: Updating the TextView here covers more cases than via onItemSelected listener.
         */
        updateAdapter(newEntries = false)

        return drawableState
    }

    override fun performClick(): Boolean {
        performDropdownEvent(isOpen = true)
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (isDropdownOpen && hasFocus) {
            performDropdownEvent(isOpen = false)
        }
    }

    /**
     * Sets the spinner's [isSelected] state to true when the dropdown is open, and false when
     * it is closed.
     */
    private fun performDropdownEvent(isOpen: Boolean) {
        isDropdownOpen = isOpen
        // Only set state if there is no error
        isSelected = if (!hasError) isOpen else false
        post { refreshDrawableState() }
    }

    companion object {

        private val STATE_ERROR = intArrayOf(R.attr.state_error)
    }
}
