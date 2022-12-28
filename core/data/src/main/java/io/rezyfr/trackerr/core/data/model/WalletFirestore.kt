package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName

data class WalletFirestore (
    @PropertyName("id")
    val id: String = "",
    @get:PropertyName("balance")
    val balance: Long = 0L,
    @get:PropertyName("name")
    val name: String = "",
    @get:PropertyName("userId")
    val userId: String = "",
)
