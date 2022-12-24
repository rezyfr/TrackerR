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

package io.rezyfr.trackerr.feature.homescreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.rezyfr.trackerr.core.data.model.TransactionModel
import io.rezyfr.trackerr.feature.homescreen.ui.HomeScreenUiState.Success

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Column() {
        viewModel.uiState.collectAsState().value.let { state ->
            when (state) {
                is HomeScreenUiState.Loading -> {
                }
                is Error -> {
                    Text("Error:")
                }
                is Success -> {
                    LazyColumn() {
                        items(state.data) { transaction ->
                            TransactionItem(transaction = transaction)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}


@Composable
fun TransactionItem(transaction: TransactionModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = transaction.description.orEmpty())
            Text(text = transaction.amount.toString())
        }
    }
}
// Previews

//@Preview(showBackground = true)
//@Composable
//private fun DefaultPreview() {
//    MyApplicationTheme {
//        TransactionScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
//    }
//}
//
//@Preview(showBackground = true, widthDp = 480)
//@Composable
//private fun PortraitPreview() {
//    MyApplicationTheme {
//        TransactionScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
//    }
//}
