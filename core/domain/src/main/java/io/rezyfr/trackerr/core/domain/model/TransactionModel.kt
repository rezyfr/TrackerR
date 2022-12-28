package io.rezyfr.trackerr.core.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.data.model.AddTransactionFirestore
import io.rezyfr.trackerr.core.data.model.TransactionFirestore
import io.rezyfr.trackerr.core.domain.mapper.DateUtils
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import java.util.*

data class TransactionModel(
    val id: String,
    val amount: Long,
    val date: String,
    val description: String,
    @TransactionType val type: String,
    val wallet: String,
    val walletId: String,
    val category: String,
    val categoryId: String,
) {
    val isIncome = type == TransactionType.INCOME
    val amountLabel: String = (if (!isIncome) "-" else "") + NumberUtils.getRupiahCurrency(amount)

    fun asAddTransactionFirestore(
        uid: String,
        walletRef: DocumentReference,
        categoryRef: DocumentReference
    ) = AddTransactionFirestore(
        amount = amount,
        date = Timestamp(DateUtils.getDateFromDetailDate(date) ?: Date()),
        description = description,
        type = type,
        walletRef = walletRef,
        categoryRef = categoryRef,
        userId = uid
    )
}

fun TransactionFirestore.asDomainModel() = TransactionModel(
    id = id.orEmpty(),
    amount = amount ?: 0L,
    date = DateUtils.getDetailDateString(date?.toDate()).orEmpty(),
    description = description.orEmpty(),
    type = type.orEmpty(),
    wallet = wallet?.name.orEmpty(),
    category = category?.name.orEmpty(),
    walletId = wallet?.id.orEmpty(),
    categoryId = category?.id.orEmpty()
)

val previewTransactionModel = listOf(
    TransactionModel(
        id = "id1",
        amount = 1000000L,
        date = "24 Oktober 2022",
        description = "Traktir ulang tahun",
        type = TransactionType.EXPENSE,
        wallet = "BCA",
        category = "Keluarga",
        walletId = "waId",
        categoryId = "catId"
    ),
    TransactionModel(
        id = "id2",
        amount = 56000L,
        date = "28 Oktober 2022",
        description = "Starbucks Salted Caramel Latte",
        type = TransactionType.EXPENSE,
        wallet = "Mandiri",
        category = "Jajanan",
        walletId = "waId",
        categoryId = "catId"
    ),
    TransactionModel(
        id = "id3",
        amount = 729000L,
        date = "1 November 2022",
        description = "PLN Oktober 2022",
        type = TransactionType.EXPENSE,
        wallet = "Mandiri",
        category = "Tagihan - Listrik",
        walletId = "waId",
        categoryId = "catId"
    ),
    TransactionModel(
        id = "id4",
        amount = 14328000L,
        date = "25 November 2022",
        description = "Gaji November 2022",
        type = TransactionType.INCOME,
        wallet = "Mandiri",
        category = "Gaji",
        walletId = "waId",
        categoryId = "catId"
    ),
)