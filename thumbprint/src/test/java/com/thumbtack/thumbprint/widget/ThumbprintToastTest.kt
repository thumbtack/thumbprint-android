package com.thumbtack.thumbprint.widget

import android.content.Context
import android.os.Looper.getMainLooper
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.thumbtack.thumbprint.MaterialTestApplication
import com.thumbtack.thumbprint.R
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/** Tests for [@ThumbprintToast] */
@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
class ThumbprintToastTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val container = CoordinatorLayout(context)

    @Test
    fun messageString() {
        ThumbprintToast().withContainer(container).withMessage("foo").show()
        shadowOf(getMainLooper()).idle()

        val snackbarTextView = getSnackbarTextView()
        assertThat(snackbarTextView).isNotNull()
        snackbarTextView?.let {
            assertThat(it.text).isEqualTo("foo")
        }
    }

    @Test
    fun messageResource() {
        ThumbprintToast().withContainer(container).withMessage(R.string.just_now).show()
        shadowOf(getMainLooper()).idle()

        val snackbarTextView = getSnackbarTextView()
        assertThat(snackbarTextView).isNotNull()
        snackbarTextView?.let {
            assertThat(it.text).isEqualTo("Just now")
        }
    }

    private fun getSnackbarTextView() = container.findViewById<TextView>(R.id.snackbar_text)
}
