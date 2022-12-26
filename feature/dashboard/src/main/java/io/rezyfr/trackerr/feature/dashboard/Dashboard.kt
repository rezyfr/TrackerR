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

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.rezyfr.trackerr.core.domain.model.previewTransactionModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.TextCell
import io.rezyfr.trackerr.core.ui.component.TransactionItem
import io.rezyfr.trackerr.feature.dashboard.HomeScreenUiState.Success

@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val dashboardState by viewModel.uiState.collectAsState()

    DashboardScreen(dashboardState = dashboardState)
    viewModel.uiState.collectAsState().value.let { state ->
        when (state) {
            is HomeScreenUiState.Loading -> {
            }
            is Error -> {
                Text("Error:")
            }
            is Success -> {
            }
            else -> {}
        }
    }
}

@Composable
fun DashboardScreen(
    dashboardState: HomeScreenUiState,
) {
    LazyColumn {
        item {
            TextCell(label = "Recent transaction", actionText = "See all")
        }
        when (dashboardState) {
            is Success -> {
                items(dashboardState.data) { transaction ->
                    TransactionItem(transaction = transaction)
                    Divider()
                }
            }
            else -> Unit
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    TrTheme {
        DashboardScreen(dashboardState = HomeScreenUiState.Success(previewTransactionModel))
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PortraitPreview() {
    TrTheme {
        DashboardScreen(dashboardState = HomeScreenUiState.Success(previewTransactionModel))
    }
}
