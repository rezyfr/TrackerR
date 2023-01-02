package io.rezyfr.trackerr.core.domain.model

import io.rezyfr.trackerr.core.data.model.WalletFirestore
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils

data class WalletModel(
    val id: String,
    val balance: Long,
    val name: String,
    val userId: String,
) {
    val balanceLabel: String = NumberUtils.getRupiahCurrency(balance)
    companion object {
        fun dummyData() = WalletModel(
            id = "12312",
            balance = 0L,
            name = "BCA",
            userId = ""
        )
    }
}

fun WalletFirestore.asDomainModel() = WalletModel(
    balance = balance,
    name = name,
    userId = userId,
    id = id
)

data class SelectableWalletModel(
    val wallet: WalletModel,
    val selected: Boolean
)