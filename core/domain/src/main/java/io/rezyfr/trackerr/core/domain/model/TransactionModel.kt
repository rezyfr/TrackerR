package io.rezyfr.trackerr.core.domain.model

import io.rezyfr.trackerr.core.data.mapper.DateUtils
import io.rezyfr.trackerr.core.data.mapper.NumberUtils
import io.rezyfr.trackerr.core.data.model.TransactionFirestore

data class TransactionModel(
    val amount: String,
    val date: String,
    val description: String,
    val type: String,
    val wallet: String,
    val category: String,
) {
    val isIncome = type == "income"

    companion object {
        fun empty() = TransactionModel(
            amount = "",
            date = "",
            description = "",
            type = "",
            wallet = "",
            category = "",
        )
    }
}

fun TransactionFirestore.asUiModel() = TransactionModel(
    amount = "${
        (if (type == "expense") "-" else "") + NumberUtils.getRupiahCurrency(
            NumberUtils.getCleanString(
                amount.orEmpty()
            )
        )
    } ",
    date = DateUtils.getDetailDateString(date?.toDate()).orEmpty(),
    description = description.orEmpty(),
    type = type.orEmpty(),
    wallet = wallet?.name.orEmpty(),
    category = category?.name.orEmpty(),
)

val previewTransactionModel = listOf(
    TransactionModel(
        amount = "Rp 1.000.000",
        date = "24 Oktober 2022",
        description = "Traktir ulang tahun",
        type = "expense",
        wallet = "BCA",
        category = "Keluarga"
    ),
    TransactionModel(
        amount = "Rp 56.000",
        date = "28 Oktober 2022",
        description = "Starbucks Salted Caramel Latte",
        type = "expense",
        wallet = "Mandiri",
        category = "Jajanan"
    ),
    TransactionModel(
        amount = "Rp 729.000",
        date = "1 November 2022",
        description = "PLN Oktober 2022",
        type = "expense",
        wallet = "Mandiri",
        category = "Tagihan - Listrik"
    ),
    TransactionModel(
        amount = "Rp 14.328.000",
        date = "25 November 2022",
        description = "Gaji November 2022",
        type = "income",
        wallet = "Mandiri",
        category = "Gaji"
    ),
)