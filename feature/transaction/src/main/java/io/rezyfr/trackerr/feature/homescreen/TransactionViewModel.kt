package io.rezyfr.trackerr.feature.homescreen

import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import io.rezyfr.trackerr.core.ui.component.BottomSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor() :
    SimpleFlowViewModel<TransactionState, TransactionEvent>() {

    private val walletBotomSheet = BottomSheet()

    override val initialUi: TransactionState = TransactionState(
        trxType = TransactionType.EXPENSE,
        trxDesc = "",
        trxWallet = null,
        walletBottomSheet = walletBotomSheet
    )

    @TransactionType
    var trxType = MutableStateFlow(initialUi.trxType)
    var trxDesc = MutableStateFlow(initialUi.trxDesc)
    var trxWallet = MutableStateFlow(initialUi.trxWallet)

    override val uiFlow: Flow<TransactionState> = combine(
        trxType, trxDesc, trxWallet
    ) { type, desc, wallet ->
        TransactionState(
            trxType = type,
            trxDesc = desc,
            trxWallet = wallet,
            walletBottomSheet = walletBotomSheet
        )
    }

    override suspend fun handleEvent(event: TransactionEvent) {
        when (event) {
            TransactionEvent.Initial -> {

            }
            is TransactionEvent.WalletSelected -> {
                handleWalletSelected(event)
            }
            is TransactionEvent.DescriptionChange -> {
                handleDescriptionChange(event)
            }
            is TransactionEvent.TypeSelected -> {
                handleTypeSelected(event)
            }
        }
    }

    private fun handleTypeSelected(event: TransactionEvent.TypeSelected) {
        trxType.value = event.type
    }

    private fun handleWalletSelected(event: TransactionEvent.WalletSelected) {
        trxWallet.value = event.wallet
    }

    private fun handleDescriptionChange(event: TransactionEvent.DescriptionChange) {
        trxDesc.value = event.descriptionChange
    }
}

data class TransactionState(
    @TransactionType val trxType: String,
    val trxDesc: String,
    val trxWallet: WalletModel?,
    val walletBottomSheet: BottomSheet
)

sealed interface TransactionEvent {
    object Initial : TransactionEvent
    data class TypeSelected(val type: String) : TransactionEvent
    data class WalletSelected(val wallet: WalletModel?) : TransactionEvent
    data class DescriptionChange(val descriptionChange: String) : TransactionEvent
}