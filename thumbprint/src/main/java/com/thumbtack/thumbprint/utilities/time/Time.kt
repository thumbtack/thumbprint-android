package com.thumbtack.thumbprint.utilities.time

import android.content.Context
import com.thumbtack.thumbprint.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Adapted from
 * https://github.com/thumbtack/website/blob/master/thumbprint/utilities/time/time.es6.js
 * in order to have consistency across web and mobile
 */
@Suppress("MagicNumber")
object Time {
    val ONE_MIN_IN_MS: Long = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
    val ONE_HOUR_IN_MS: Long = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
    val ONE_DAY_IN_MS: Long = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
    val ONE_WEEK_IN_MS: Long = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS)
    val ONE_MONTH_IN_MS: Long = TimeUnit.MILLISECONDS.convert(7 * 4, TimeUnit.DAYS)
    val ONE_YEAR_IN_MS: Long = TimeUnit.MILLISECONDS.convert(365, TimeUnit.DAYS)
    val TWO_YEAR_IN_MS: Long = TimeUnit.MILLISECONDS.convert(2 * 365, TimeUnit.DAYS)

    val dateFormat = SimpleDateFormat("MM/yy", Locale.US)
}

/**
 * @param timestamp, a unix timestamp in MS
 *
 * @return string for the elapsed time rounded by unit.
 *
 * Note: a month is approximated by 28 days
 *
 * `Just now`: X <1min
 * `Xm`: 1min <= X < 60min (e.g. `5m` for 5 min ago)
 * `Xh`: 1hr <= X < 24hr (e.g. `12h` for 12hours ago)
 * `Xd`: 24hr <= X < 7 days (e.g. `1d`)
 * `Xw`: 7days <= X < 28 days (e.g. `3wk`)
 * `Xmo`: 30 days <= X < 365 days (e.g. `10mo`)
 * `1yr`: 365 days <= X < 2 years
 * `MM/YY`: X >=2yrs (e.g. `11/15`)
 */
fun Long.makeElapsedTimeString(timestamp: Long, context: Context): String {
    val elapsedMiliseconds = this - timestamp

    return when {
        elapsedMiliseconds < Time.ONE_MIN_IN_MS -> context.getString(R.string.just_now)

        elapsedMiliseconds < Time.ONE_HOUR_IN_MS -> String.format(
            Locale.US,
            "%d%s",
            elapsedMiliseconds / Time.ONE_MIN_IN_MS,
            context.getString(R.string.minute)
        )

        elapsedMiliseconds < Time.ONE_DAY_IN_MS -> String.format(
            Locale.US,
            "%d%s",
            elapsedMiliseconds / Time.ONE_HOUR_IN_MS,
            context.getString(R.string.hour)
        )

        elapsedMiliseconds < Time.ONE_WEEK_IN_MS -> String.format(
            Locale.US,
            "%d%s",
            elapsedMiliseconds / Time.ONE_DAY_IN_MS,
            context.getString(R.string.day)
        )

        elapsedMiliseconds < Time.ONE_MONTH_IN_MS -> String.format(
            Locale.US,
            "%d%s",
            elapsedMiliseconds / Time.ONE_WEEK_IN_MS,
            context.getString(R.string.week)
        )

        elapsedMiliseconds < Time.ONE_YEAR_IN_MS -> String.format(
            Locale.US,
            "%d%s",
            elapsedMiliseconds / Time.ONE_MONTH_IN_MS,
            context.getString(R.string.month)
        )

        elapsedMiliseconds < Time.TWO_YEAR_IN_MS -> context.getString(R.string.one_year)

        else -> Time.dateFormat.format(timestamp)
    }
}
