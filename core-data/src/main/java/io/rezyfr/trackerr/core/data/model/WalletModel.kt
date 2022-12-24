package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName

data class WalletModel(
    @get:PropertyName("wallet")
    val wallet: String = "",
    @get:PropertyName("name")
    val name: String = "",
)
