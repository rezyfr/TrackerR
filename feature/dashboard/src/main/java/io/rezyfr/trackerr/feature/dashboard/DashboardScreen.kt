

package io.rezyfr.trackerr.feature.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import io.rezyfr.trackerr.core.domain.model.previewTransactionModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.TextCell
import io.rezyfr.trackerr.feature.dashboard.component.AccountBalance
import io.rezyfr.trackerr.feature.dashboard.component.DashboardTopBar
import io.rezyfr.trackerr.feature.transaction.component.TransactionItem
import io.rezyfr.trackerr.feature.transaction.model.TransactionUiModel
import io.rezyfr.trackerr.feature.transaction.model.asUiModel

interface DashboardNavigator {
    fun openTransactionWithId(trxId: String)
    fun openNewTransactionDialog()
    fun navigateUp()
}

@Destination
@Composable
@OptIn(ExperimentalLifecycleComposeApi::class)
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    navigator: DashboardNavigator
) {
    val recentTransactionState by viewModel.recentTransactionState.collectAsStateWithLifecycle()
    val balanceState by viewModel.totalBalanceState.collectAsStateWithLifecycle()
    DashboardScreen(
        recentTransactionState = recentTransactionState,
        totalBalanceState = balanceState,
        modifier = modifier,
        onTransactionClick = { navigator.openTransactionWithId(it.id) },
        showTransactionSheet = { navigator.openNewTransactionDialog() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    recentTransactionState: RecentTransactionState,
    totalBalanceState: TotalBalanceState,
    modifier: Modifier = Modifier,
    onTransactionClick: (TransactionUiModel) -> Unit = {},
    showTransactionSheet: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            AddNewTrxFab(onFabClick = showTransactionSheet)
        }
    ) {
        LazyColumn(modifier = modifier.padding(it)) {
            accountTotalBalance(totalBalanceState)
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            recentTransaction(
                recentTransactionState = recentTransactionState,
                onTransactionClick = onTransactionClick,
                onNoTransactionClick = showTransactionSheet
            )
        }
    }
}

@Composable
fun AddNewTrxFab(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit = {}
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onFabClick,
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
    ) {
        Icon(Icons.Filled.Add, contentDescription = null)
    }
}

fun LazyListScope.accountTotalBalance(totalBalanceState: TotalBalanceState) {
    when (totalBalanceState) {
        is TotalBalanceState.Success -> {
            item {
                DashboardTopBar(
                    profileUrl = totalBalanceState.profileUrl,
                    selectedMonth = "October",
                )
            }
            item {
                AccountBalance(balance = totalBalanceState.balance)
            }
        }
        is TotalBalanceState.Error -> {
            item {
                Text(text = "Error" + totalBalanceState.throwable.message)
            }
        }
        else -> Unit
    }
}

fun LazyListScope.recentTransaction(
    recentTransactionState: RecentTransactionState,
    onTransactionClick: (TransactionUiModel) -> Unit,
    onNoTransactionClick: () -> Unit
) {
    when (recentTransactionState) {
        is RecentTransactionState.Success -> {
            item {
                TextCell(label = "Recent transaction", actionText = "See all")
            }
            if (recentTransactionState.data.isNotEmpty()) {
                items(recentTransactionState.data) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        modifier = Modifier.clickable { onTransactionClick(transaction) }
                    )
                    Divider()
                }
            } else {
                item {
                    EmptyTransactionView(onNoTransactionClick)
                }
            }
        }
        else -> Unit
    }
}

@Composable
private fun EmptyTransactionView(onNoTransactionClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .clickable { onNoTransactionClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.ReceiptLong,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
        Text(
            text = "No transaction yet",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Text(
            text = "Tap to add your first transaction",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TransactionPreview() {
    TrTheme {
        DashboardScreen(
            recentTransactionState = RecentTransactionState.Success(
                previewTransactionModel.map { it.asUiModel() }
            ),
            totalBalanceState = TotalBalanceState.Success(
                "Rp 1.000.000",
                "https://lh3.googleusercontent.com/a/AEdFTp45oBYXyei183tTlYUjAeQbdt1nBEIZRS0-Om4a=s96-c"
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EmptyTransactionPreview() {
    TrTheme {
        DashboardScreen(
            recentTransactionState = RecentTransactionState.Success(listOf()),
            totalBalanceState = TotalBalanceState.Success(
                "Rp 1.000.000",
                "https://lh3.googleusercontent.com/a/AEdFTp45oBYXyei183tTlYUjAeQbdt1nBEIZRS0-Om4a=s96-c"
            ),
        )
    }
}
