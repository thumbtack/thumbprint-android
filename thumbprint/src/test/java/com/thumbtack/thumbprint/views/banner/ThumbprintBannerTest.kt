package com.thumbtack.thumbprint.views.banner

import android.text.SpannedString
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.thumbtack.thumbprint.ClickListenerSpan
import com.thumbtack.thumbprint.MaterialTestApplication
import kotlinx.android.synthetic.main.thumbprint_banner.view.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** Tests for [ThumbprintBanner] */
@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
class ThumbprintBannerTest {

    @Test
    fun clickLink_notifiedListener() {
        var linkClicked = false
        val testLinkText = "some link text"
        val bannerView = ThumbprintBanner(getApplicationContext()).apply {
            linkText = testLinkText
            linkClickListener = { linkClicked = true }
        }
        // Simulate clicking on link.
        SpannedString(bannerView.bannerText.text)
            .getSpans(0, testLinkText.length, ClickListenerSpan::class.java)
            ?.first()
            ?.onClick(bannerView.bannerText)

        assertTrue(linkClicked)
    }
}
