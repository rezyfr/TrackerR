package io.rezyfr.trackerr.feature.transaction

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.mapper.formatToUi
import io.rezyfr.trackerr.core.domain.mapper.fromUiToLocaleDate
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.domain.usecase.AddTransactionUseCase
import io.rezyfr.trackerr.core.domain.usecase.GetTransactionByIdUseCase
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import io.rezyfr.trackerr.core.ui.component.BottomSheet
import io.rezyfr.trackerr.feature.transaction.model.TransactionUiModel
import io.rezyfr.trackerr.feature.transaction.model.asUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase
) : SimpleFlowViewModel<TransactionState, TransactionEvent>() {

    private val walletBottomSheet = BottomSheet()
    private val categoryBottomSheet = BottomSheet()
    private val dateBottomSheet = BottomSheet()

    override val initialUi: TransactionState = TransactionState(
        trx = TransactionUiModel.emptyData(),
        walletBottomSheet = walletBottomSheet,
        categoryBottomSheet = categoryBottomSheet,
        dateBottomSheet = dateBottomSheet,
        saveTransactionResult = ResultState.Uninitialized,
        enabledButton = false
    )

    @TransactionType
    private var trx = MutableStateFlow(initialUi.trx)
    private var trxAddResult = MutableStateFlow(initialUi.saveTransactionResult)

    override val uiFlow: Flow<TransactionState> = combine(
        trx, trxAddResult
    ) { trx, result ->
        TransactionState(
            trx = trx,
            walletBottomSheet = walletBottomSheet,
            categoryBottomSheet = categoryBottomSheet,
            dateBottomSheet = dateBottomSheet,
            saveTransactionResult = result,
            enabledButton = trx.category.id.isNotEmpty() && trx.wallet.id.isNotEmpty() && trx.amount != 0L
        )
    }

    override suspend fun handleEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.Initial -> {
                handleInitial(event.trxId)
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
            is TransactionEvent.OnSelectDate -> {
                handleDateSelected(event)
            }
        }
    }

    private fun handleInitial(trxId: String) {
        viewModelScope.launch {
            getTransactionByIdUseCase(trxId).collectLatest {
                trx.value = it.asUiModel()
            }
        }
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            val transaction = trx.value ?: return@launch

            val result = addTransactionUseCase(
                TransactionModel(
                    amount = transaction.amount,
                    description = transaction.description.ifEmpty { transaction.category.name },
                    type = transaction.type,
                    wallet = transaction.wallet,
                    category = transaction.category,
                    id = "",
                    date = transaction.date.fromUiToLocaleDate()
                )
            )
            trxAddResult.value = result
        }
    }

    private fun handleAmountChange(event: TransactionEvent.OnChangeAmount) {
        if (event.amount.text.isEmpty()) {
            trx.value = trx.value.copy(amount = 0)
            return
        }
        val amount = NumberUtils.getCleanString(event.amount.text)
        val formattedAmount = NumberUtils.getNominalFormat(amount)
        trx.value = trx.value.copy(
            amount = amount,
            amountLabel = TextFieldValue(formattedAmount, TextRange(formattedAmount.length))
        )
    }

    private fun handleTypeSelected(event: TransactionEvent.OnSelectType) {
        trx.value = trx.value.copy(type = event.type)
    }

    private fun handleDateSelected(event: TransactionEvent.OnSelectDate) {
        trx.value = trx.value.copy(date = event.date.formatToUi())
    }

    private fun handleWalletSelected(event: TransactionEvent.OnSelectWallet) {
        trx.value = trx.value.copy(wallet = event.wallet)
    }

    private fun handleCategorySelected(event: TransactionEvent.OnSelectCategory) {
        trx.value = trx.value.copy(category = event.category)
    }

    private fun handleDescriptionChange(event: TransactionEvent.OnChangeDescription) {
        trx.value = trx.value.copy(description = event.description)
    }

}

data class TransactionState(
    val trx: TransactionUiModel,
    val walletBottomSheet: BottomSheet,
    val categoryBottomSheet: BottomSheet,
    val dateBottomSheet: BottomSheet,
    val saveTransactionResult: ResultState<Nothing?>,
    val enabledButton: Boolean
)

sealed interface TransactionEvent {
    data class Initial(val trxId: String) : TransactionEvent
    object OnSaveTransaction : TransactionEvent
    data class OnSelectType(val type: String) : TransactionEvent
    data class OnSelectWallet(val wallet: WalletModel) : TransactionEvent
    data class OnSelectCategory(val category: CategoryModel) : TransactionEvent
    data class OnChangeDescription(val description: String) : TransactionEvent

    data class OnChangeAmount(val amount: TextFieldValue) : TransactionEvent
    data class OnSelectDate(val date: LocalDate) : TransactionEvent
}
