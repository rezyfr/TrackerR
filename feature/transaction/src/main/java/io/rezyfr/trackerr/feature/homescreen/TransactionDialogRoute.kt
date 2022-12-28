package io.rezyfr.trackerr.feature.homescreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.*
import io.rezyfr.trackerr.core.ui.typeIndicatorColor
import io.rezyfr.trackerr.feature.homescreen.component.TransactionTextField
import io.rezyfr.trackerr.feature.homescreen.ui.category.CategoryPickerBottomSheet
import io.rezyfr.trackerr.feature.homescreen.ui.wallet.WalletPickerBottomSheet
import java.util.*

@Composable
fun TransactionDialogRoute(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    viewModel: TransactionViewModel = hiltViewModel(),
) {
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
            onCloseClick = {
                transitionDialogHelper::triggerAnimatedClose.invoke()
            },
            modifier = modifier,
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun TransactionDialog(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
) {
    CircularReveal(
        targetState = state.trxType.typeIndicatorColor(),
        animationSpec = tween(1000)
    ) {
        Box(
            modifier
                .background(color = it)
                .fillMaxSize()
        ) {
            TransactionAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                state = state,
                onCloseClick = onCloseClick,
                onEvent = onEvent
            )
            TransactionForm(
                Modifier
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                onEvent = onEvent,
                state = state
            )

            WalletPickerBottomSheet(
                bottomSheet = state.walletBottomSheet,
                selected = state.trxWallet,
                onSelect = { wallet ->
                    onEvent(TransactionEvent.OnSelectWallet(wallet))
                }
            )
            CategoryPickerBottomSheet(
                bottomSheet = state.categoryBottomSheet,
                selected = state.trxCategory,
                type = state.trxType,
                onSelect = { category ->
                    onEvent(TransactionEvent.OnSelectCategory(category))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAppBar(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            MultiSelector(
                options = listOf(TransactionType.EXPENSE, TransactionType.INCOME),
                selectedOption = state.trxType,
                onOptionSelect = { type ->
                    onEvent(TransactionEvent.OnSelectType(type))
                },
                backgroundColor = state.trxType.typeIndicatorColor(),
                selectedHighlightColor = MaterialTheme.colorScheme.background,
                selectedColor = state.trxType.typeIndicatorColor(),
                unselectedColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .height(36.dp)
                    .padding(horizontal = 36.dp)
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(
                onClick = onCloseClick
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    onEvent: (TransactionEvent) -> Unit,
    state: TransactionState
) {
    Column(modifier) {
        AmountTextField(
            Modifier,
            state = state,
            onEvent = onEvent
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
                value = state.trxCategory?.name.orEmpty(),
                onClick = {
                    state.categoryBottomSheet.expand()
                }
            )
            TransactionTextField(
                placeholder = "Description",
                showTrailingIcon = false,
                onValueChange = { onEvent.invoke(TransactionEvent.OnChangeDescription(it)) },
                value = state.trxDesc
            )
            TransactionTextField(
                placeholder = "Wallet",
                value = state.trxWallet?.name.orEmpty(),
                onClick = {
                    state.walletBottomSheet.expand()
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            SaveTransactionButton(
                onClick = { onEvent.invoke(TransactionEvent.OnSaveTransaction) },
            )
        }
    }
}

@Composable
fun SaveTransactionButton(
    onClick: () -> Unit = {},
) {
    TrButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = 12.dp,
            horizontal = 16.dp
        )
    ) {
        Text(
            text = "Save",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun AmountTextField(
    modifier: Modifier = Modifier,
    onEvent: (TransactionEvent) -> Unit,
    state: TransactionState
) {
    Column(modifier.padding(16.dp)) {
        Text(
            "how much?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.65f)
        )
        BasicTextField(
            visualTransformation = PrefixTransformation("Rp"),
            value = state.trxAmountLabel,
            onValueChange = {
                onEvent.invoke(TransactionEvent.OnChangeAmount(it))
            },
            textStyle = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.background
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.background),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrxDialog() {
    TrTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
//            TransactionDialog(
//                state = TransactionState()
//            )
        }
    }
}
