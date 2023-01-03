package io.rezyfr.trackerr.core.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import io.rezyfr.trackerr.core.domain.mapper.toLocalDate
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import java.time.LocalDate

data class TransactionFirestore(
    @get:PropertyName("id")
    val id: String? = null,
    @get:PropertyName("amount")
    val amount: Long? = null,
    @get:PropertyName("date")
    val date: Timestamp? = null,
    @get:PropertyName("description")
    val description: String? = null,
    @get:PropertyName("type")
    val type: String? = null,
    @get:PropertyName("wallet")
    val wallet: WalletFirestore? = null,
    @get:PropertyName("category")
    val category: CategoryFirestore? = null,
    @get:PropertyName("walletRef")
    val walletRef: DocumentReference? = null,
    @get:PropertyName("categoryRef")
    val categoryRef: DocumentReference? = null,
)

fun TransactionFirestore.asDomainModel() = TransactionModel(
    id = id.orEmpty(),
    amount = amount ?: 0L,
    date = date?.toLocalDate() ?: LocalDate.now(),
    description = description.orEmpty(),
    type = type.orEmpty(),
    wallet = wallet?.asDomainModel() ?: WalletModel.emptyData(),
    category = category?.asDomainModel() ?: CategoryModel.emptyData(),
    walletRef = walletRef,
    categoryRef = categoryRef,
)
