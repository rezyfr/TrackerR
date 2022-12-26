package io.rezyfr.trackerr.core.data.mapper

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object NumberUtils {
    var localeIndonesia = Locale("in", "id")
    private var indonesiaFormat = DecimalFormat("###,###.##", indonesiaFormat())
    private fun indonesiaFormat(): DecimalFormatSymbols {
        var symbols: DecimalFormatSymbols? = null
        try {
            symbols = DecimalFormatSymbols.getInstance(localeIndonesia)
        } catch (e: Exception) {
            //ignore
        }

        if (symbols == null) {
            symbols = DecimalFormatSymbols()
            symbols.decimalSeparator = ','
            symbols.currencySymbol = "Rp"
            symbols.groupingSeparator = '.'
        }

        return symbols
    }

    fun getNominalFormat(amount: Long): String {
        return indonesiaFormat.format(amount)
    }

    fun getRupiahCurrency(amount: Long): String {
        val moneyString = indonesiaFormat.format(kotlin.math.abs(amount))
        return if (amount < 0) "-Rp$moneyString"
        else "Rp$moneyString"
    }

    fun getShortCurrency(amount: Long): String {
        val shortCurrencyPairs = listOf<Pair<Long, String>>(
            Pair(1, ""),
            Pair(1_000, "rb"),
            Pair(1_000_000, "jt"),
            Pair(1_000_000_000, "miliar"),
            Pair(1_000_000_000_000, "triliun")
        )
        val (shortAmount, suffix) = shortCurrencyPairs.find { amount < it.first * 1000 }?.let {
            val amountWithDecimal = roundOffDecimal(amount.toDouble() / it.first)
            Pair(amountWithDecimal, it.second)
        } ?: Pair(amount, "")
        return if (amount < 0) "- Rp$shortAmount$suffix"
        else "Rp$shortAmount$suffix"
    }

    fun getCleanString(s: String): Long {
        val cleanString = s.replace("[,.]".toRegex(), "")
        return try {
            val parsed = cleanString.toLong()
            parsed
        } catch (e: Exception) {
            0L
        }
    }

    private fun roundOffDecimal(number: Double): String? {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_EVEN
        return df.format(number)
    }
}