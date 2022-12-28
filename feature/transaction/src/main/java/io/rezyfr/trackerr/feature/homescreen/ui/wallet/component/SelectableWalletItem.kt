package io.rezyfr.trackerr.feature.homescreen.ui.wallet.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.domain.model.SelectableWalletModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.WrapContentRow

@Composable
fun WalletPickerRow(
    wallets: List<SelectableWalletModel>,
    modifier: Modifier = Modifier,
    onSelect: (SelectableWalletModel) -> Unit = {}
) {
    WrapContentRow(
        modifier = modifier.padding(8.dp),
        items = wallets,
        itemKey = { it.wallet.id }) { wallet ->
        SelectableWalletItem(item = wallet, onSelect = { onSelect(wallet) })
    }
}

@Composable
fun SelectableWalletItem(
    item: SelectableWalletModel,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val wallet = item.wallet
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                when (item.selected) {
                    true -> Modifier.background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp)
                    )
                    false -> Modifier.border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp)
                    )
                }
            )
            .clickable(onClick = onSelect)
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        val color =
            if (item.selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
        Text(
            text = wallet.name,
            style = MaterialTheme.typography.titleSmall,
            color = color
        )
    }
}

@Composable
@Preview
private fun Preview() {
    TrTheme {
        WalletPickerRow(
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
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "5", balance = 10000, userId = "0",
                        name = "Wallet 5"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "6", balance = 10000, userId = "0",
                        name = "Wallet 6"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "7", balance = 10000, userId = "0",
                        name = "Wallet 7"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "8", balance = 10000, userId = "0",
                        name = "Wallet 8"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "9", balance = 10000, userId = "0",
                        name = "Wallet 9"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "10", balance = 10000, userId = "0",
                        name = "Wallet 10"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "11", balance = 10000, userId = "0",
                        name = "Wallet 11"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "12", balance = 10000, userId = "0",
                        name = "Wallet 12"
                    ),
                    selected = false
                ),
                SelectableWalletModel(
                    wallet = WalletModel(
                        id = "13",
                        balance = 10000,
                        userId = "0",
                        name = "Wallet 13"
                    ),
                    selected = false
                )
            )
        )
    }
}