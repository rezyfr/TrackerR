package io.rezyfr.trackerr.core.domain.mapper

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private fun LocalDate.format(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

fun LocalDate.formatToUi(format: String = "dd MMMM yyyy", includeWeekDay: Boolean = false): String {
    if(!includeWeekDay) return this.format(format)
    else {
        val today = LocalDate.now()
        val isThisYear = today.year == this.year

        return when (this) {
            today -> {
                "Today, ${this.format(format)}"
            }
            today.minusDays(1) -> {
                "Yesterday, ${this.format(format)}"
            }
            today.plusDays(1) -> {
                "Tomorrow, ${this.format(format)}"
            }
            else -> {
                if (isThisYear) {
                    this.format("EEEE, dd MMMM")
                } else {
                    this.format("dd MMMM yyyy")
                }
            }
        }
    }
}

fun Timestamp.toLocalDate(): LocalDate {
    return this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun LocalDate.formatToDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun String.fromUiToLocaleDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return LocalDate.parse(this, formatter)
}