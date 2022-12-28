/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rezyfr.trackerr.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.rezyfr.trackerr.core.domain.model.previewTransactionModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.TextCell
import io.rezyfr.trackerr.core.ui.component.TransactionItem

@Composable
fun DashboardRoute(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val recentTransactionState by viewModel.recentTransactionState.collectAsState()
    val balanceState by viewModel.totalBalanceState.collectAsState()
    DashboardScreen(
        recentTransactionState = recentTransactionState,
        totalBalanceState = balanceState,
        modifier = modifier,
    )
}

@Composable
fun DashboardScreen(
    recentTransactionState: RecentTransactionState,
    totalBalanceState: TotalBalanceState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        accountTotalBalance(totalBalanceState)
        item() {
            TextCell(label = "Recent transaction", actionText = "See all")
        }
        recentTransaction(recentTransactionState = recentTransactionState)
    }
}

fun LazyListScope.accountTotalBalance(totalBalanceState: TotalBalanceState) {
    when (totalBalanceState) {
        is TotalBalanceState.Success -> {
            item {
                Row(Modifier.fillMaxWidth().padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = totalBalanceState.profileUrl,
                        contentDescription = "Profile image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(48.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Total balance",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = totalBalanceState.balance,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
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

fun LazyListScope.recentTransaction(recentTransactionState: RecentTransactionState) {
    when (recentTransactionState) {
        is RecentTransactionState.Success -> {
            items(recentTransactionState.data) { transaction ->
                TransactionItem(transaction = transaction)
                Divider()
            }
        }
        else -> Unit
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PortraitPreview() {
    TrTheme {
        DashboardScreen(
            recentTransactionState = RecentTransactionState.Success(
                previewTransactionModel
            ),
            totalBalanceState = TotalBalanceState.Success(
                "Rp 1.000.000",
                "https://lh3.googleusercontent.com/a/AEdFTp45oBYXyei183tTlYUjAeQbdt1nBEIZRS0-Om4a=s96-c"
            ),
        )
    }
}
