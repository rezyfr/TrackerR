package io.rezyfr.trackerr.core.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class TransactionModel(
    @get:PropertyName("amount")
    val amount: String? = null,
    @get:PropertyName("date")
    val date: Timestamp? = null,
    @get:PropertyName("description")
    val description: String? = null,
    @get:PropertyName("type")
    val type: String? = null,
    @get:PropertyName("wallet")
    val wallet: WalletModel? = null,
    @get:PropertyName("category")
    val category: CategoryModel? = null,
)
