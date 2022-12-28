package io.rezyfr.trackerr.feature.homescreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.CircularReveal
import io.rezyfr.trackerr.core.ui.component.ModalTransitionDialog
import io.rezyfr.trackerr.core.ui.component.MultiSelector
import io.rezyfr.trackerr.core.ui.component.TrButton
import io.rezyfr.trackerr.core.ui.typeIndicatorColor
import io.rezyfr.trackerr.feature.homescreen.component.TransactionTextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDialog(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
    @TransactionType type: String = TransactionType.EXPENSE
) {
    var selectedType by remember { mutableStateOf(type) }
    CircularReveal(
        targetState = selectedType.typeIndicatorColor(),
        animationSpec = tween(1000)
    ) {
        Box(
            modifier
                .background(color = it)
                .fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    MultiSelector(
                        options = listOf(TransactionType.EXPENSE, TransactionType.INCOME),
                        selectedOption = selectedType,
                        onOptionSelect = {
                            selectedType = it
                        },
                        backgroundColor = selectedType.typeIndicatorColor(),
                        selectedHighlightColor = MaterialTheme.colorScheme.background,
                        selectedColor = selectedType.typeIndicatorColor(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
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
                    onEvent(TransactionEvent.WalletSelected(wallet))
                }
            )
        }
    }
}

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    onEvent: (TransactionEvent) -> Unit,
    state: TransactionState
) {
    Column(modifier) {
        AmountTextField(Modifier)
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
                placeholder = "Category"
            )
            TransactionTextField(
                placeholder = "Description",
                showTrailingIcon = false,
                onValueChange = { onEvent.invoke(TransactionEvent.DescriptionChange(it)) },
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
                onClick = {},
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
fun AmountTextField(modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Text(
            "how much?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.65f)
        )
        BasicTextField(
            value = "Rp0",
            onValueChange = {

            },
            textStyle = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.background
            ),
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
