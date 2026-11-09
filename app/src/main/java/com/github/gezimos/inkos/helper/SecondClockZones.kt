package com.github.gezimos.inkos.helper

import android.content.Context
import com.github.gezimos.inkos.R
import java.util.Locale
import java.util.TimeZone

/**
 * Curated list of IANA timezones offered for the secondary (dual) clock.
 *
 * The secondary clock previously only supported a fixed UTC offset in hours, which drifts out of
 * sync twice a year for anyone whose family lives across a DST boundary (see inkOS issue #113).
 * Picking an actual zone id instead keeps the clock correct all year round.
 */
object SecondClockZones {

    private val ZONE_IDS: List<String> = listOf(
        // UTC
        "Etc/UTC",
        // Pacific & Oceania
        "Pacific/Honolulu",
        "Pacific/Auckland",
        "Australia/Perth",
        "Australia/Sydney",
        // East Asia
        "Asia/Tokyo",
        "Asia/Seoul",
        "Asia/Shanghai",
        "Asia/Hong_Kong",
        "Asia/Taipei",
        "Asia/Manila",
        "Asia/Singapore",
        // Southeast Asia
        "Asia/Jakarta",
        "Asia/Bangkok",
        "Asia/Ho_Chi_Minh",
        // South Asia
        "Asia/Kolkata",
        "Asia/Karachi",
        // Middle East
        "Asia/Dubai",
        "Asia/Tehran",
        "Asia/Jerusalem",
        "Asia/Baghdad",
        "Asia/Riyadh",
        // Africa
        "Africa/Lagos",
        "Africa/Cairo",
        "Africa/Johannesburg",
        "Africa/Nairobi",
        // Europe
        "Europe/Moscow",
        "Europe/Istanbul",
        "Europe/Kyiv",
        "Europe/Athens",
        "Europe/Helsinki",
        "Europe/Warsaw",
        "Europe/Prague",
        "Europe/Budapest",
        "Europe/Vienna",
        "Europe/Zurich",
        "Europe/Rome",
        "Europe/Berlin",
        "Europe/Madrid",
        "Europe/Paris",
        "Europe/Amsterdam",
        "Europe/Stockholm",
        "Europe/Dublin",
        "Europe/London",
        "Europe/Lisbon",
        // Americas
        "America/Argentina/Buenos_Aires",
        "America/Sao_Paulo",
        "America/Bogota",
        "America/New_York",
        "America/Toronto",
        "America/Chicago",
        "America/Mexico_City",
        "America/Denver",
        "America/Los_Angeles",
        "America/Vancouver",
        "America/Anchorage",
    )

    /** ids list plus a leading blank entry meaning "no timezone — use offset hours". */
    fun idsWithOff(): List<String> = listOf("") + ZONE_IDS

    /** Human-readable label: "Region/City · GMT+08:00" (or the localized off entry). */
    fun displayLabel(context: Context, id: String): String {
        if (id.isBlank()) return context.getString(R.string.second_clock_timezone_off)
        val tz = TimeZone.getTimeZone(id)
        return "$id  ·  ${gmtLabel(tz)}"
    }

    /** Short label for compact settings rows: blank → "–", else GMT offset. */
    fun shortLabel(id: String): String {
        if (id.isBlank()) return "–"
        val tz = TimeZone.getTimeZone(id)
        return gmtLabel(tz)
    }

    /** True when the id parses to an actual zone (unknown ids silently resolve to GMT). */
    fun isValid(id: String): Boolean {
        if (id.isBlank()) return true
        val tz = TimeZone.getTimeZone(id)
        return tz.id == id
    }

    private fun gmtLabel(tz: TimeZone): String {
        val offsetMs = tz.getOffset(System.currentTimeMillis())
        val minutes = offsetMs / 60000
        val sign = if (minutes >= 0) "+" else "-"
        val abs = if (minutes < 0) -minutes else minutes
        val hours = abs / 60
        val mins = abs % 60
        return String.format(Locale.US, "GMT%s%02d:%02d", sign, hours, mins)
    }
}
