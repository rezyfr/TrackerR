package io.rezyfr.trackerr.feature.transaction.ui.wallet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.domain.model.SelectableWalletModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.BottomSheet
import io.rezyfr.trackerr.core.ui.component.BottomSheetTitle
import io.rezyfr.trackerr.core.ui.component.TrxBottomSheet
import io.rezyfr.trackerr.core.ui.util.hiltViewModelPreviewSafe
import io.rezyfr.trackerr.feature.transaction.ui.wallet.component.WalletPickerRow

@Composable
fun BoxScope.WalletPickerBottomSheet(
    bottomSheet: BottomSheet,
    selected: WalletModel? = null,
    onSelect: (WalletModel) -> Unit = {}
) {
    val viewModel: WalletPickerViewModel? = hiltViewModelPreviewSafe()
    val walletPickerState = viewModel?.uiState?.collectAsState()?.value ?: previewState()

    LaunchedEffect(key1 = selected) {
        viewModel?.onEvent(WalletPickerEvent.WalletSelected(selected))
    }

    TrxBottomSheet(bottomSheet = bottomSheet) {
        LazyColumn() {
            item(key = "title") {
                BottomSheetTitle(text = "Choose Wallet")
            }
            if (walletPickerState is WalletPickerState.Success) {
                walletItems(
                    items = walletPickerState.wallets,
                    onWalletSelect = {
                        onSelect(it)
                        bottomSheet.collapse()
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

private fun LazyListScope.walletItems(
    items: List<SelectableWalletModel>,
    onWalletSelect: (WalletModel) -> Unit
) {
    item {
        WalletPickerRow(
            wallets = items,
            onSelect = { onWalletSelect(it.wallet) }
        )
    }
}

@Preview
@Composable
fun Preview() {
    TrTheme {
        Box {
            WalletPickerBottomSheet(
                bottomSheet = BottomSheet().apply {
                    expand()
                },
                selected = WalletModel(
                    id = "1",
                    name = "Wallet 1",
                    balance = 1000000,
                    userId = "1"
                )
            ) {}
        }
    }
}

private fun previewState() = WalletPickerState.Success(
    wallets = listOf(
        SelectableWalletModel(
            wallet = WalletModel(
                id = "1", balance = 10000, userId = "0",
                name = "Wallet 1"
            ),
            selected = true
        ),
        SelectableWalletModel(
            wallet = WalletModel(
                id = "2", balance = 10000, userId = "0",
                name = "Wallet 2"
            ),
            selected = false
        ),
        SelectableWalletModel(
            wallet = WalletModel(
                id = "3", balance = 10000, userId = "0",
                name = "Wallet 3"
            ),
            selected = false
        ),
        SelectableWalletModel(
            wallet = WalletModel(
                id = "4", balance = 10000, userId = "0",
                name = "Wallet 4"
            ),
            selected = false
        ),
    ),
    selectedWallet = WalletModel(
        id = "1", balance = 10000, userId = "0",
        name = "Wallet 1"
    )
)
