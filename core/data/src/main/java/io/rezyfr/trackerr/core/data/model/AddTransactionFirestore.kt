package io.rezyfr.trackerr.core.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class AddTransactionFirestore (
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
    @get:PropertyName("walletRef")
    val walletRef: DocumentReference? = null,
    @get:PropertyName("categoryRef")
    val categoryRef: DocumentReference? = null,
    @get:PropertyName("userId")
    val userId: String? = null,
)
