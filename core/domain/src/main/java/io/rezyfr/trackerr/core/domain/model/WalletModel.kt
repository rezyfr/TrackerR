package io.rezyfr.trackerr.core.domain.model

import io.rezyfr.trackerr.core.domain.mapper.NumberUtils

data class WalletModel(
    val id: String,
    val balance: Long,
    val name: String,
    val userId: String,
) {
    val balanceLabel: String = NumberUtils.getRupiahCurrency(balance)
    companion object {
        fun emptyData() = WalletModel(
            id = "",
            balance = 0L,
            name = "",
            userId = ""
        )
    }
}

data class SelectableWalletModel(
    val wallet: WalletModel,
    val selected: Boolean
)