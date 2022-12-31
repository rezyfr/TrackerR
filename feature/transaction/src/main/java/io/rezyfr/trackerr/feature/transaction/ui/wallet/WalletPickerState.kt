package io.rezyfr.trackerr.feature.transaction.ui.wallet

import io.rezyfr.trackerr.core.domain.model.SelectableWalletModel
import io.rezyfr.trackerr.core.domain.model.WalletModel

sealed interface WalletPickerState {
    object Uninitialized : WalletPickerState
    data class Success(val wallets: List<SelectableWalletModel>, val selectedWallet: WalletModel?) :
        WalletPickerState

    data class Error(val error: Throwable) : WalletPickerState
}
