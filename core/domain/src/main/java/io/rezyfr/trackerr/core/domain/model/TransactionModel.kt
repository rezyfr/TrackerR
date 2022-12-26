package io.rezyfr.trackerr.core.domain.model

import io.rezyfr.trackerr.core.domain.mapper.DateUtils
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.data.model.TransactionFirestore

data class TransactionModel(
    val amount: Long,
    val date: String,
    val description: String,
    val type: String,
    val wallet: String,
    val category: String,
) {
    val isIncome = type == "income"
    val amountLabel: String =
        (if (type == "expense") "-" else "") + NumberUtils.getRupiahCurrency(amount)

    companion object {
        fun empty() = TransactionModel(
            amount = 0L,
            date = "",
            description = "",
            type = "",
            wallet = "",
            category = "",
        )
    }
}

fun TransactionFirestore.asUiModel() = TransactionModel(
    amount = amount ?: 0L,
    date = DateUtils.getDetailDateString(date?.toDate()).orEmpty(),
    description = description.orEmpty(),
    type = type.orEmpty(),
    wallet = wallet?.name.orEmpty(),
    category = category?.name.orEmpty(),
)

val previewTransactionModel = listOf(
    TransactionModel(
        amount = 1000000L,
        date = "24 Oktober 2022",
        description = "Traktir ulang tahun",
        type = "expense",
        wallet = "BCA",
        category = "Keluarga"
    ),
    TransactionModel(
        amount = 56000L,
        date = "28 Oktober 2022",
        description = "Starbucks Salted Caramel Latte",
        type = "expense",
        wallet = "Mandiri",
        category = "Jajanan"
    ),
    TransactionModel(
        amount = 729000L,
        date = "1 November 2022",
        description = "PLN Oktober 2022",
        type = "expense",
        wallet = "Mandiri",
        category = "Tagihan - Listrik"
    ),
    TransactionModel(
        amount = 14328000L,
        date = "25 November 2022",
        description = "Gaji November 2022",
        type = "income",
        wallet = "Mandiri",
        category = "Gaji"
    ),
)