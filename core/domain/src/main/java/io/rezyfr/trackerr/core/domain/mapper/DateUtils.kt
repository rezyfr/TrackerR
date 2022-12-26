package io.rezyfr.trackerr.core.domain.mapper

import android.os.Build
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils.localeIndonesia
import java.text.*
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    const val A_DAY: Long = 86_400_000L

    fun getDetailDateString(serverDate: Date?): String? {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", localeIndonesia)
        return serverDate?.let { dateFormat.format(serverDate) }
    }

    fun getDetailDateString(serverDate: String?): String? {
        val date = getDateFromServer(serverDate)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", localeIndonesia)
        return date?.let { dateFormat.format(date) }
    }

    fun getDetailDateAndDayString(serverDate: String?): String? {
        val date = getDateFromServer(serverDate)
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", localeIndonesia)
        return date?.let { dateFormat.format(date) }
    }

    fun getDetailMinimDateString(serverDate: String?): String? {
        val date = getDateFromServer(serverDate)
        val dateFormat = SimpleDateFormat("dd MMM yyyy", localeIndonesia)
        return date?.let { dateFormat.format(date) }
    }

    fun getDetailMinimTimeString(serverDate: String?): String? {
        return getDetailTime(getDateFromServer(serverDate) ?: Date())
    }

    fun getDateMonth(date: Date): String {
        val calCurrent = Calendar.getInstance()
        val calDate = Calendar.getInstance().apply { time = date }
        return if (calCurrent.get(Calendar.YEAR) == calDate.get(Calendar.YEAR)) {
            SimpleDateFormat("MMMM", localeIndonesia).format(date)
        } else {
            SimpleDateFormat("MMM ''yy", localeIndonesia).format(date)
        }
    }

    fun getDetailTime(date: Date): String {
        return SimpleDateFormat("HH:mm:ss", localeIndonesia).format(date)
    }

    fun getTime(date: Date): String {
        return SimpleDateFormat("HH:mm", localeIndonesia).format(date)
    }

    fun getDateQuery(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", localeIndonesia).format(date)
    }

    fun getReversedDateQuery(date: Date): String {
        return SimpleDateFormat("dd-MM-yyyy", localeIndonesia).format(date)
    }

    fun getDailyLabel(date: Date): String {
        return SimpleDateFormat("dd/MM", localeIndonesia).format(date)
    }

    fun getWeeklyLabel(date: Date): String {
        return SimpleDateFormat("'W'W/MM", localeIndonesia).format(date)
    }

    fun getMonthlyLabel(date: Date): String {
        return SimpleDateFormat("MM/yy", localeIndonesia).format(date)
    }

    fun parseDateQuery(dateString: String): Date {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", localeIndonesia)
            dateFormat.parse(dateString) ?: Date()
        } catch (e: ParseException) {
            e.printStackTrace()
            Date()
        }
    }

    fun parseMonthInteger(month: Int): String {
        return DateFormatSymbols(localeIndonesia).months[month - 1]
    }

    fun getDay(date: Date): String {
        return SimpleDateFormat("dd", localeIndonesia).format(date)
    }

    fun getDatePeriod(date: Date): String {
        return SimpleDateFormat("dd MMM yy", localeIndonesia).format(date)
    }

    fun getShortDatePeriod(date: Date): String {
        return SimpleDateFormat("dd MMM ''yy", localeIndonesia).format(date)
    }

    fun getMonthAndDay(date: Date): String {
        return SimpleDateFormat("dd MMMM", localeIndonesia).format(date)
    }

    fun getMonthPeriod(date: Date): String {
        return SimpleDateFormat("MMMM yyyy", localeIndonesia).format(date)
    }

    fun getShortMonthPeriod(date: Date): String {
        return SimpleDateFormat("MMM ''yy", localeIndonesia).format(date)
    }

    fun getCurrentMonthPeriod(periodDate: Int): String {
        val date = Date()
        val day = getDay(date)
        val addMonth = when {
            periodDate >= 15 && day.toInt() > periodDate -> 1
            periodDate < 15 && day.toInt() < periodDate -> -1
            else -> 0
        }
        return getMonthPeriod(date.addMonth(addMonth))
    }

    fun getMonth(date: Date): String {
        return SimpleDateFormat("MMM", localeIndonesia).format(date)
    }

    fun getYearPeriod(date: Date): String {
        return SimpleDateFormat("yyyy", localeIndonesia).format(date)
    }

    fun getDateFromDetailDate(stringDate: String): Date? {
        return try {
            val dateFormat =
                SimpleDateFormat("dd MMMM yyyy", localeIndonesia) // 2022-07-05T15:31:51.000Z
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            dateFormat.parse(stringDate ?: "")
        } catch (e: ParseException) {
            null
        }
    }

    fun getDateFromServer(stringDate: String?): Date? {
        return try {
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                localeIndonesia
            ) // 2022-07-05T15:31:51.000Z
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            dateFormat.parse(stringDate ?: "")
        } catch (e: ParseException) {
            try {
                val dateFormat = SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    localeIndonesia
                ) // 2022-07-05T15:31:51Z
                dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                dateFormat.parse(stringDate ?: "")
            } catch (e: ParseException) {
                try {
                    val dateFormat = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        localeIndonesia
                    ) // 2022-01-01 00:00:00
                    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                    dateFormat.parse(stringDate ?: "")
                } catch (e: ParseException) {
                    try {
                        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ssX",
                                localeIndonesia
                            ) // 22-07-05T15:31:51+07:00
                        } else {
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", localeIndonesia)
                        }
                        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                        dateFormat.parse(stringDate ?: "")
                    } catch (e: ParseException) {
                        try {
                            val dateFormat = SimpleDateFormat(
                                "yyyy-MM-dd",
                                localeIndonesia
                            ) // 22-07-05T15:31:51+07:00
                            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                            dateFormat.parse(stringDate ?: "")
                        } catch (e: ParseException) {
                            null
                        }
                    }
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun setDateForServer(date: Date?): String? {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", localeIndonesia)
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            dateFormat.format(date ?: "")
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun setDateForServerNew(date: Date?): Pair<String, String>? {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", localeIndonesia)
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")

            val timeFormat = SimpleDateFormat("HH:mm:ss", localeIndonesia)
            timeFormat.timeZone = TimeZone.getTimeZone("GMT")
            Pair(dateFormat.format(date ?: ""), timeFormat.format(date ?: ""))
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val calDate1 = Calendar.getInstance().also { it.time = date1 }
        val calDate2 = Calendar.getInstance().also { it.time = date2 }
        return calDate1[Calendar.YEAR] == calDate2[Calendar.YEAR] &&
            calDate1[Calendar.MONTH] == calDate2[Calendar.MONTH] &&
            calDate1[Calendar.DATE] == calDate2[Calendar.DATE]
    }

    fun isSameMonth(date1: Date, date2: Date): Boolean {
        val calDate1 = Calendar.getInstance().also { it.time = date1 }
        val calDate2 = Calendar.getInstance().also { it.time = date2 }
        return calDate1[Calendar.YEAR] == calDate2[Calendar.YEAR] &&
            calDate1[Calendar.MONTH] == calDate2[Calendar.MONTH]
    }

    fun timeBreakdown(timeMillis: Long): String {
        var millis = timeMillis
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        millis -= TimeUnit.DAYS.toMillis(days)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millis)

        var time = ""
        if (days > 1) {
            time += "$days hari "
        }

        time += String.format(
            "%d : %d : %d",
            hours, minutes, seconds
        )

        return time
    }

    fun millisToMinutesBreakdown(timeMillis: Long): String {
        val f: NumberFormat = DecimalFormat("00")
        val min: Long = timeMillis / 60000 % 60
        val sec: Long = timeMillis / 1000 % 60
        return "${f.format(min)}:${f.format(sec)}"
    }

    fun incrementMonth(date: Date, inc: Int): Date {
        val cal = Calendar.getInstance().apply { time = date }
        return cal.apply { add(Calendar.MONTH, inc) }.time
    }

    fun getDateRange(month: Int, year: Int): Pair<String, String> {
        val date = Calendar.getInstance().apply {
            set(Calendar.DATE, 15)
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
        }.time
        val startDate = date.getStartOfMonth()
        val endDate = date.getEndOfMonth()
        return Pair(
            setDateForServer(startDate).orEmpty(),
            setDateForServer(endDate).orEmpty()
        )
    }

    fun getDateRangeNewParsed(month: Int, year: Int): Pair<String, String> {
        val date = Calendar.getInstance().apply {
            set(Calendar.DATE, 15)
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
        }.time
        val startDate = date.getStartOfMonth()
        val endDate = date.getEndOfMonth()
        return Pair(
            getDateQuery(startDate).orEmpty(),
            getDateQuery(endDate).orEmpty()
        )
    }

    fun getRelativeTimeSpanMillis(date: String): Long {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", localeIndonesia)
            val parsedDate = format.parse(date)
            parsedDate?.time?.minus(Calendar.getInstance().timeInMillis) ?: 0
        } catch (e: Exception) {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", localeIndonesia)
            val parsedDate = format.parse(date)
            parsedDate?.time?.minus(Calendar.getInstance().timeInMillis) ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun parsedRelativeTimeSpan(timeSpan: String, date: String): Pair<String, Boolean> {
        val wordPair = listOf(
            "days" to "hari",
            "hours" to "jam",
            "minutes" to "menit"
        )
        var pair = ""
        var moreThanMonth = false
        run breaking@{
            when {
                timeSpan.contains("Yesterday") -> {
                    pair = ""
                    return@breaking
                }
                timeSpan.any { it.isDigit() } -> {
                    pair = getDetailMinimDateString(date) ?: timeSpan
                    moreThanMonth = true
                }
                else -> {
                    wordPair.forEach {
                        if (timeSpan.contains(it.first)) {
                            pair = it.second
                            return@breaking
                        }
                    }
                }
            }
        }
        return Pair(pair.removeSuffix("ago"), moreThanMonth)
    }

    fun Date.getCalendarInstance(): Calendar = Calendar.getInstance().also {
        it.time = this
    }

    fun Date.addDay(n: Int): Date = getCalendarInstance().also {
        it.add(Calendar.DAY_OF_MONTH, n)
    }.time

    fun Date.addMonth(n: Int): Date = getCalendarInstance().also {
        it.add(Calendar.MONTH, n)
    }.time

    fun Date.addYear(n: Int): Date = getCalendarInstance().also {
        it.add(Calendar.YEAR, n)
    }.time

    fun Date.getStartOfDay(): Date = Calendar.getInstance().also {
        it.setZeroOClock(this)
    }.time

    fun Date.getEndOfDay(): Date = Calendar.getInstance().also {
        it.setZeroOClock(this)
        it.add(Calendar.DATE, 1)
        it.add(Calendar.MILLISECOND, -1)
    }.time

    fun Date.getStartOfMonth(): Date = Calendar.getInstance().also {
        it.setZeroOClock(this)
        it.set(Calendar.DAY_OF_MONTH, 1)
    }.time

    fun Date.getEndOfMonth(): Date = Calendar.getInstance().also {
        it.setZeroOClock(this)
        it.set(Calendar.DATE, 1)
        it.add(Calendar.MONTH, 1)
        it.add(Calendar.MILLISECOND, -1)
    }.time

    fun Date.getStartOfYear(): Date = Calendar.getInstance().also {
        it.setZeroOClock(this)
        it.set(Calendar.DAY_OF_MONTH, 1)
        it.set(Calendar.MONTH, 0)
    }.time

    fun Date.getEndOfYear(): Date = Calendar.getInstance().also {
        it.setZeroOClock(this)
        it.set(Calendar.DAY_OF_MONTH, 1)
        it.set(Calendar.MONTH, 0)
        it.add(Calendar.YEAR, 1)
        it.add(Calendar.MILLISECOND, -1)
    }.time

    fun Calendar.setZeroOClock(date: Date? = null) {
        date?.let { time = it }
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    /**
     * Return total difference days of 2 date [now] and [older].
     * Can return Negative value.
     */
    fun daysDifference(from: Date, to: Date): Long {
        val fromCalendar = from.getStartOfDay()
        val toCalendar = to.getStartOfDay()

        val millisDiff = fromCalendar.time - toCalendar.time
        return millisDiff / A_DAY
    }

    fun returnCorrectPeriodMonth(startDate: Date, endDate: Date): String {
        return if (isSameMonth(startDate, endDate)) getMonthPeriod(startDate)
        else {
            val startCal = Calendar.getInstance().apply {
                time = startDate
            }
            if (startCal.get(Calendar.DAY_OF_MONTH) > 15) {
                getMonthPeriod(endDate)
            } else getMonthPeriod(startDate)
        }
    }
}