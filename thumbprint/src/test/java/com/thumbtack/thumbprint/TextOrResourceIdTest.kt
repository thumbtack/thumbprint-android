package com.thumbtack.thumbprint

import android.content.Context
import android.os.Parcel
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** Tests for [TextOrResourceId] */
@RunWith(RobolectricTestRunner::class)
class TextOrResourceIdTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun toStringWithContext() {
        val withString = TextOrResourceId.create("foo")
        val withResource = TextOrResourceId.create(R.string.just_now)
        assertThat(withString.toString(context)).isEqualTo("foo")
        assertThat(withResource.toString(context)).isEqualTo("Just now")
    }

    @Test
    fun toStringWithResource() {
        val withString = TextOrResourceId.create("foo")
        val withResource = TextOrResourceId.create(R.string.just_now)
        assertThat(withString.toString(context.resources)).isEqualTo("foo")
        assertThat(withResource.toString(context.resources)).isEqualTo("Just now")
    }

    @Test
    fun parcelableText() {
        val tori = TextOrResourceId.create("foo")
        val parcel = Parcel.obtain()
        tori.writeToParcel(parcel, 0)
        // reset for reading
        parcel.setDataPosition(0)
        val toriRecreated = Text.createFromParcel(parcel)
        assertThat(toriRecreated).isEqualTo(tori)
    }

    @Test
    fun parcelableResourceId() {
        val tori = TextOrResourceId.create(R.string.just_now)
        val parcel = Parcel.obtain()
        tori.writeToParcel(parcel, 0)
        // reset for reading
        parcel.setDataPosition(0)
        val toriRecreated = ResourceId.createFromParcel(parcel)
        assertThat(toriRecreated).isEqualTo(tori)
    }
}
