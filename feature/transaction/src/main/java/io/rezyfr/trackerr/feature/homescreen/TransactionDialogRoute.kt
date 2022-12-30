package io.rezyfr.trackerr.feature.homescreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.domain.mapper.fromUiToLocaleDate
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.ui.component.ButtonText
import io.rezyfr.trackerr.core.ui.component.CircularReveal
import io.rezyfr.trackerr.core.ui.component.ModalTransitionDialog
import io.rezyfr.trackerr.core.ui.component.TrButton
import io.rezyfr.trackerr.core.ui.typeIndicatorColor
import io.rezyfr.trackerr.feature.homescreen.component.AmountTextField
import io.rezyfr.trackerr.feature.homescreen.component.TransactionAppBar
import io.rezyfr.trackerr.feature.homescreen.component.TransactionTextField
import io.rezyfr.trackerr.feature.homescreen.model.TransactionUiModel
import io.rezyfr.trackerr.feature.homescreen.ui.category.CategoryPickerBottomSheet
import io.rezyfr.trackerr.feature.homescreen.ui.datepicker.DatePickerBottomSheet
import io.rezyfr.trackerr.feature.homescreen.ui.wallet.WalletPickerBottomSheet
import java.time.LocalDate
import io.rezyfr.trackerr.core.ui.icon.Icon as AppIcon

@Composable
fun TransactionDialogRoute(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    viewModel: TransactionViewModel = hiltViewModel(),
    trx: TransactionUiModel? = null
) {
    if(trx != null) {
        viewModel.onEvent(TransactionEvent.Initial(trx))
    }
    val state by viewModel.uiState.collectAsState()
    ModalTransitionDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
    ) { transitionDialogHelper ->
        if (state.saveTransactionResult is ResultState.Success) {
            transitionDialogHelper::triggerAnimatedClose.invoke()
        }
        TransactionDialog(
            modifier = modifier,
            state = state,
            onCloseClick = { transitionDialogHelper::triggerAnimatedClose.invoke() },
            onSelectType = { (viewModel::onEvent)(TransactionEvent.OnSelectType(it)) },
            onSelectWallet = { (viewModel::onEvent)(TransactionEvent.OnSelectWallet(it)) },
            onChangeAmount = { (viewModel::onEvent)(TransactionEvent.OnChangeAmount(it)) },
            onChangeDescription = { (viewModel::onEvent)(TransactionEvent.OnChangeDescription(it)) },
            onSelectCategory = { (viewModel::onEvent)(TransactionEvent.OnSelectCategory(it)) },
            onPickDate = { (viewModel::onEvent)(TransactionEvent.OnSelectDate(it)) },
            onSaveTransaction = { (viewModel::onEvent)(TransactionEvent.OnSaveTransaction) }
        )
    }
}

@Composable
fun TransactionDialog(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    state: TransactionState,
    onSelectType: (String) -> Unit,
    onSelectWallet: (WalletModel) -> Unit,
    onChangeAmount: (TextFieldValue) -> Unit,
    onChangeDescription: (String) -> Unit,
    onSelectCategory: (CategoryModel) -> Unit,
    onPickDate: (LocalDate) -> Unit,
    onSaveTransaction: () -> Unit,
) {
    CircularReveal(
        targetState = state.trx.type.typeIndicatorColor(),
        animationSpec = tween(1000)
    ) { color ->
        Box(
            modifier
                .background(color = color)
                .fillMaxSize()
        ) {
            TransactionAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                type = state.trx.type,
                onCloseClick = onCloseClick,
                onSelectType = onSelectType,
            )
            TransactionForm(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                state = state,
                onAmountChange = onChangeAmount,
                onDescriptionChange = onChangeDescription,
                onSaveTransaction = onSaveTransaction
            )
            WalletPickerBottomSheet(
                bottomSheet = state.walletBottomSheet,
                selected = state.trx.wallet,
                onSelect = onSelectWallet
            )
            CategoryPickerBottomSheet(
                bottomSheet = state.categoryBottomSheet,
                selected = state.trx.category,
                type = state.trx.type,
                onSelect = onSelectCategory
            )
            DatePickerBottomSheet(
                bottomSheet = state.dateBottomSheet,
                startDate = state.trx.date.fromUiToLocaleDate(),
                onPick = onPickDate
            )
        }
    }
}

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    onAmountChange: (TextFieldValue) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveTransaction: () -> Unit,
    state: TransactionState
) {
    Column(modifier) {
        AmountTextField(
            Modifier.fillMaxWidth(),
            value = state.trx.amountLabel,
            onValueChange = onAmountChange
        )
        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        topStart = 32.dp, topEnd = 32.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TransactionTextField(
                placeholder = "Category",
                trailingIcon = AppIcon.ImageVectorIcon(Icons.Default.ArrowDropDown),
                value = state.trx.category.name,
                onClick = {
                    state.categoryBottomSheet.expand()
                }
            )
            TransactionTextField(
                placeholder = "Description",
                onValueChange = onDescriptionChange,
                value = state.trx.description
            )
            TransactionTextField(
                placeholder = "Wallet",
                trailingIcon = AppIcon.ImageVectorIcon(Icons.Default.ArrowDropDown),
                value = state.trx.wallet.name,
                onClick = {
                    state.walletBottomSheet.expand()
                }
            )
            TransactionTextField(
                placeholder = "Date",
                value = state.trx.date,
                trailingIcon = AppIcon.ImageVectorIcon(Icons.Default.DateRange),
                onClick = {
                    state.dateBottomSheet.expand()
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            SaveTransactionButton(
                onClick = onSaveTransaction,
                enabledButton = state.enabledButton
            )
        }
    }
}

@Composable
fun SaveTransactionButton(
    onClick: () -> Unit = {},
    enabledButton: Boolean
) {
    TrButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        text = { ButtonText("Save") },
        enabled = enabledButton,
    )
}