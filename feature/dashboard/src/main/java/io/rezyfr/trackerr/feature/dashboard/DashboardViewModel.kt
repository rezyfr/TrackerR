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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.usecase.GetCurrentUserProfileUseCase
import io.rezyfr.trackerr.core.domain.usecase.GetRecentTransactionUseCase
import io.rezyfr.trackerr.core.domain.usecase.GetTotalBalanceUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getRecentTransactionUseCase: GetRecentTransactionUseCase,
    getTotalBalanceUseCase: GetTotalBalanceUseCase,
    getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    val recentTransactionState: StateFlow<RecentTransactionState> =
        getRecentTransactionUseCase()
            .catch {
                RecentTransactionState.Error(it)
            }
            .map { RecentTransactionState.Success(data = it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                RecentTransactionState.Loading
            )

    val totalBalanceState: StateFlow<TotalBalanceState> = combine(
        getTotalBalanceUseCase.invoke(),
        getCurrentUserProfileUseCase.invoke()
    ) { totalBalance, user ->
        TotalBalanceState.Success(
            balance = NumberUtils.getRupiahCurrency(totalBalance),
            profileUrl = user.photoUrl
        )
    }.catch {
        TotalBalanceState.Error(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TotalBalanceState.Loading
    )

}

sealed interface TotalBalanceState {
    object Loading : TotalBalanceState
    data class Error(val throwable: Throwable) : TotalBalanceState
    data class Success(
        val balance: String,
        val profileUrl: String
    ) : TotalBalanceState
}

sealed interface RecentTransactionState {
    object Loading : RecentTransactionState
    data class Error(val throwable: Throwable) : RecentTransactionState
    data class Success(val data: List<TransactionModel>) : RecentTransactionState
}