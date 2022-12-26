package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName

data class WalletFirestore (
    @get:PropertyName("balance")
    val balance: String = "",
    @get:PropertyName("name")
    val name: String = "",
    @get:PropertyName("userId")
    val userId: String = "",
)
