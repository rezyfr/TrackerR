package io.rezyfr.trackerr.feature.homescreen

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.domain.usecase.AddTransactionUseCase
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import io.rezyfr.trackerr.core.ui.component.BottomSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : SimpleFlowViewModel<TransactionState, TransactionEvent>() {

    private val walletBotomSheet = BottomSheet()
    private val categoryBottomSheet = BottomSheet()

    override val initialUi: TransactionState = TransactionState(
        trxType = TransactionType.EXPENSE,
        trxDesc = "",
        trxWallet = null,
        trxAmount = 0,
        trxAmountLabel = TextFieldValue("0"),
        trxCategory = null,
        walletBottomSheet = walletBotomSheet,
        categoryBottomSheet = categoryBottomSheet,
        saveTransactionResult = ResultState.Uninitialized
    )

    @TransactionType
    private var trxType = MutableStateFlow(initialUi.trxType)
    private var trxDesc = MutableStateFlow(initialUi.trxDesc)
    private var trxWallet = MutableStateFlow(initialUi.trxWallet)
    private var trxCategory = MutableStateFlow(initialUi.trxCategory)
    private var trxAmount = MutableStateFlow(initialUi.trxAmount)
    private var trxAmountLabel = MutableStateFlow(initialUi.trxAmountLabel)
    private var trxAddResult = MutableStateFlow(initialUi.saveTransactionResult)

    private fun trxAmountFlow() = combine(trxAmount, trxAmountLabel) { amount, label ->
        Pair(amount, label)
    }

    private fun trxPickerFlow() = combine(trxWallet, trxCategory) { wallet, category ->
        Pair(wallet, category)
    }

    override val uiFlow: Flow<TransactionState> = combine(
        trxType, trxDesc, trxPickerFlow(), trxAmountFlow(), trxAddResult
    ) { type, desc, (wallet, category), (amount, amountLabel), result ->
        TransactionState(
            trxType = type,
            trxDesc = desc,
            trxWallet = wallet,
            trxCategory = category,
            trxAmount = amount,
            trxAmountLabel = amountLabel,
            walletBottomSheet = walletBotomSheet,
            categoryBottomSheet = categoryBottomSheet,
            saveTransactionResult = result,
        )
    }

    override suspend fun handleEvent(event: TransactionEvent) {
        when (event) {
            TransactionEvent.Initial -> {

            }
            TransactionEvent.OnSaveTransaction -> {
                saveTransaction()
            }
            is TransactionEvent.OnSelectWallet -> {
                handleWalletSelected(event)
            }
            is TransactionEvent.OnSelectCategory -> {
                handleCategorySelected(event)
            }
            is TransactionEvent.OnChangeDescription -> {
                handleDescriptionChange(event)
            }
            is TransactionEvent.OnSelectType -> {
                handleTypeSelected(event)
            }
            is TransactionEvent.OnChangeAmount -> {
                handleAmountChange(event)
            }
        }
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            val wallet = trxWallet.value ?: return@launch
            val category = trxCategory.value ?: return@launch
            val amount = trxAmount.value
            val desc = trxDesc.value
            val type = trxType.value

            val result = addTransactionUseCase(
                TransactionModel(
                    walletId = wallet.id,
                    categoryId = category.id,
                    amount = amount,
                    description = desc,
                    type = type,
                    wallet = wallet.name,
                    category = category.name,
                    id = "",
                    date = "2022-12-12"
                )
            )
            trxAddResult.value = result
        }
    }

    private fun handleAmountChange(event: TransactionEvent.OnChangeAmount) {
        if (event.amount.text.isEmpty()) {
            trxAmount.value = 0
            trxAmountLabel.value = event.amount
            return
        }
        trxAmount.value = NumberUtils.getCleanString(event.amount.text)
        val formattedAmount = NumberUtils.getNominalFormat(trxAmount.value)
        trxAmountLabel.value = TextFieldValue(formattedAmount, TextRange(formattedAmount.length))
    }

    private fun handleTypeSelected(event: TransactionEvent.OnSelectType) {
        trxType.value = event.type
    }

    private fun handleWalletSelected(event: TransactionEvent.OnSelectWallet) {
        trxWallet.value = event.wallet
    }

    private fun handleCategorySelected(event: TransactionEvent.OnSelectCategory) {
        trxCategory.value = event.category
    }

    private fun handleDescriptionChange(event: TransactionEvent.OnChangeDescription) {
        trxDesc.value = event.descriptionChange
    }

}

data class TransactionState(
    @TransactionType val trxType: String,
    val trxDesc: String,
    val trxAmount: Long,
    val trxAmountLabel: TextFieldValue,
    val trxWallet: WalletModel?,
    val trxCategory: CategoryModel?,
    val walletBottomSheet: BottomSheet,
    val categoryBottomSheet: BottomSheet,
    val saveTransactionResult: ResultState<Nothing?>
)

sealed interface TransactionEvent {
    object Initial : TransactionEvent
    object OnSaveTransaction : TransactionEvent
    data class OnSelectType(val type: String) : TransactionEvent
    data class OnSelectWallet(val wallet: WalletModel?) : TransactionEvent
    data class OnSelectCategory(val category: CategoryModel?) : TransactionEvent
    data class OnChangeDescription(val descriptionChange: String) : TransactionEvent

    data class OnChangeAmount(val amount: TextFieldValue) : TransactionEvent
}
