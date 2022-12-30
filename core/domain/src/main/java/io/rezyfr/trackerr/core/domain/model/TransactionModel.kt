package io.rezyfr.trackerr.core.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.data.model.AddTransactionFirestore
import io.rezyfr.trackerr.core.data.model.TransactionFirestore
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.mapper.formatToDate
import io.rezyfr.trackerr.core.domain.mapper.toLocalDate
import java.time.LocalDate

data class TransactionModel(
    val id: String,
    val amount: Long,
    val date: LocalDate,
    val description: String,
    @TransactionType val type: String,
    val wallet: WalletModel,
    val category: CategoryModel,
) {
    val isIncome = type == TransactionType.INCOME
    val amountLabel: String = (if (!isIncome) "-" else "") + NumberUtils.getRupiahCurrency(amount)

    fun asAddTransactionFirestore(
        uid: String,
        walletRef: DocumentReference,
        categoryRef: DocumentReference
    ) = AddTransactionFirestore(
        amount = amount,
        date = Timestamp(date.formatToDate()),
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
    date = date?.toLocalDate() ?: LocalDate.now(),
    description = description.orEmpty(),
    type = type.orEmpty(),
    wallet = wallet?.asDomainModel() ?: WalletModel.emptyData(),
    category = category?.asDomainModel() ?: CategoryModel.emptyData(),
)

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