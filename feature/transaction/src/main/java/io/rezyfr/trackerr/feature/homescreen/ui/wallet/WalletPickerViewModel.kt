package io.rezyfr.trackerr.feature.homescreen.ui.wallet

import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.core.domain.model.SelectableWalletModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.domain.usecase.GetWalletsUseCase
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class WalletPickerViewModel @Inject constructor(
    private val getWalletsUseCase: GetWalletsUseCase
) : SimpleFlowViewModel<WalletPickerState, WalletPickerEvent>() {

    private val selectedWallet = MutableStateFlow<WalletModel?>(null)
    override val initialUi: WalletPickerState = WalletPickerState.Uninitialized
    override val uiFlow: Flow<WalletPickerState>
        get() = combine(getWalletsUseCase(Unit), selectedWallet) { wallets, selectedWallet ->
            WalletPickerState.Success(wallets.map {
                SelectableWalletModel(
                    wallet = it,
                    selected = it.id == selectedWallet?.id
                )
            }, selectedWallet)
        }.catch {
            WalletPickerState.Error(it)
        }

    override suspend fun handleEvent(event: WalletPickerEvent) {
        when (event) {
            is WalletPickerEvent.WalletSelected -> {
                handleSelectedWallet(event.wallet)
            }
            is WalletPickerEvent.Initial -> {

            }
        }
    }

    private fun handleSelectedWallet(wallet: WalletModel?) {
        selectedWallet.value = wallet
    }
}