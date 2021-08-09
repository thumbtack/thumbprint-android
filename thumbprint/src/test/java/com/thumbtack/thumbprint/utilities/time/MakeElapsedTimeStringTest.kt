package com.thumbtack.thumbprint.utilities.time

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Date
import java.util.concurrent.TimeUnit

@Config(
    packageName = "com.thumbtack.thumbprint"
)
@RunWith(RobolectricTestRunner::class)
class MakeElapsedTimeStringTest {
    /**
     * Tests for time utilities to ensure they conform to Thumbprint standards
     *
     * makeElapsedTimeString(timestamp: Long, nowTimestamp: Long):
     *  - Partition on difference (X) between timestamp and nowTimestamp:
     *      - X < 1 min
     *      - 1 min <= X < 1 hr
     *      - 1 hr <= X < 24 hrs
     *      - 24 hrs <= X < 7 days
     *      - 7 days <= X < 28 days
     *      - 28 days <= X < 365 days
     *      - 365 days <= X < 2 years
     *      - 2 years <= X
     */

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = getApplicationContext()
    }

    @Test
    fun testThirtySeconds() {
        assertTimeString("Just now", TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS))
    }

    @Test
    fun testOneMinute() {
        assertTimeString("1m", TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES))
    }

    @Test
    fun testFiftyNineMinutes() {
        assertTimeString("59m", TimeUnit.MILLISECONDS.convert(59, TimeUnit.MINUTES))
    }

    @Test
    fun testOneHour() {
        assertTimeString("1h", TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))
    }

    @Test
    fun testThreeHours() {
        assertTimeString("3h", TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS))
    }

    @Test
    fun testTwentyFourHour() {
        assertTimeString("1d", TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS))
    }

    @Test
    fun testTwoDays() {
        assertTimeString("2d", TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS))
    }

    @Test
    fun testSevenDaysMinusOneMs() {
        assertTimeString("6d", TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS) - 1)
    }

    @Test
    fun testSevenDays() {
        assertTimeString("1w", TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS))
    }

    @Test
    fun testFifteenDays() {
        assertTimeString("2w", TimeUnit.MILLISECONDS.convert(15, TimeUnit.DAYS))
    }

    @Test
    fun testTwentyEightDays() {
        assertTimeString("1mo", TimeUnit.MILLISECONDS.convert(28, TimeUnit.DAYS))
    }

    @Test
    fun testTwoMonths() {
        // We approximate a month as 28 days
        assertTimeString("2mo", TimeUnit.MILLISECONDS.convert(2 * 28, TimeUnit.DAYS))
    }

    @Test
    fun testFourteenMonths() {
        assertTimeString("1y", TimeUnit.MILLISECONDS.convert(14 * 28, TimeUnit.DAYS))
    }

    @Test
    fun testThreeYears() {
        val threeYearsMs = TimeUnit.MILLISECONDS.convert(3 * 365, TimeUnit.DAYS)
        val dateMs = Date().time - threeYearsMs
        assertTimeString(Time.dateFormat.format(dateMs), threeYearsMs)
    }

    private fun assertTimeString(expected: String, timestamp: Long) {
        val timeNow = Date().time
        assertEquals(expected, timeNow.makeElapsedTimeString(timeNow - timestamp, context))
    }
}
