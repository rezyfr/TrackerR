package io.rezyfr.trackerr.core.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.mapper.formatToDate
import java.time.LocalDate

data class TransactionModel(
    val id: String,
    val amount: Long,
    val date: LocalDate,
    val description: String,
    @TransactionType val type: String,
    val wallet: WalletModel,
    val category: CategoryModel,
    val walletRef: DocumentReference? = null,
    val categoryRef: DocumentReference? = null
) {
    val isIncome = type == TransactionType.INCOME
    val amountLabel: String = (if (!isIncome) "-" else "") + NumberUtils.getRupiahCurrency(amount)

    fun asAddTransactionFirestore(
        id: String? = null,
        uid: String,
        walletRef: DocumentReference,
        categoryRef: DocumentReference
    ) = hashMapOf (
        "amount" to amount,
        "date" to Timestamp(date.formatToDate()),
        "description" to description.ifEmpty { category.name },
        "type" to type,
        "walletRef" to walletRef,
        "categoryRef" to categoryRef,
        "userId" to uid,
    ).apply {
        if (!id.isNullOrEmpty()) {
            this["id"] = id
        }
    }
}

val previewTransactionModel = listOf(
    TransactionModel(
        id = "id1",
        amount = 1000000L,
        date = LocalDate.now(),
        description = "Traktir ulang tahun",
        type = TransactionType.EXPENSE,
        wallet = WalletModel.emptyData(),
        category = CategoryModel.emptyData(),
    ),
    TransactionModel(
        id = "id2",
        amount = 56000L,
        date = LocalDate.now().plusDays(1),
        description = "Starbucks Salted Caramel Latte",
        type = TransactionType.EXPENSE,
        wallet = WalletModel.emptyData(),
        category = CategoryModel.emptyData(),
    ),
    TransactionModel(
        id = "id3",
        amount = 729000L,
        date = LocalDate.now().plusDays(2),
        description = "PLN Oktober 2022",
        type = TransactionType.EXPENSE,
        wallet = WalletModel.emptyData(),
        category = CategoryModel.emptyData(),
    ),
    TransactionModel(
        id = "id4",
        amount = 14328000L,
        date = LocalDate.now().minusDays(12),
        description = "Gaji November 2022",
        type = TransactionType.INCOME,
        wallet = WalletModel.emptyData(),
        category = CategoryModel.emptyData(),
    ),
)