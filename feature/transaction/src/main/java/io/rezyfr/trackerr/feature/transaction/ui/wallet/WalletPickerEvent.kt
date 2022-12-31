package io.rezyfr.trackerr.feature.transaction.ui.wallet

import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.model.WalletModel

sealed interface WalletPickerEvent {
    data class Initial(@TransactionType val type: String) : WalletPickerEvent
    data class WalletSelected(val wallet: WalletModel?) : WalletPickerEvent
}