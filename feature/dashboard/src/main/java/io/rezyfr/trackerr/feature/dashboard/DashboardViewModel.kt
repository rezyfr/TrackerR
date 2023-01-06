package io.rezyfr.trackerr.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.asResult
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.usecase.GetCurrentUserProfileUseCase
import io.rezyfr.trackerr.core.domain.usecase.GetRecentTransactionUseCase
import io.rezyfr.trackerr.core.domain.usecase.GetTotalBalanceUseCase
import io.rezyfr.trackerr.feature.transaction.model.TransactionUiModel
import io.rezyfr.trackerr.feature.transaction.model.asUiModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getRecentTransactionUseCase: GetRecentTransactionUseCase,
    getTotalBalanceUseCase: GetTotalBalanceUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    val recentTransactionState: StateFlow<RecentTransactionState> =
        getRecentTransactionUseCase(Unit)
            .catch {
                RecentTransactionState.Error(it)
            }
            .map {
                if (it is ResultState.Success) {
                    RecentTransactionState.Success(data = it.data.map { it.asUiModel(withWeekDay = true) })
                } else {
                    RecentTransactionState.Error((it as ResultState.Error).exception ?: Exception())
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                RecentTransactionState.Loading
            )

    val totalBalanceState: StateFlow<TotalBalanceState> = getTotalBalanceUseCase.invoke(Unit).asResult()
        .map { userWithBalance ->
            when (userWithBalance) {
                is ResultState.Success -> {
                    val (balance, user) = Pair(userWithBalance.data, getCurrentUserProfileUseCase(Unit))
                    TotalBalanceState.Success(
                        balance = NumberUtils.getRupiahCurrency(balance),
                        profileUrl = (user as? ResultState.Success)?.data?.photoUrl.orEmpty()
                    )
                }
                is ResultState.Error -> {
                    TotalBalanceState.Error(userWithBalance.exception ?: Exception())
                }
                is ResultState.Loading -> {
                    TotalBalanceState.Loading
                }
                else -> {
                    TotalBalanceState.Uninitialized
                }
            }
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
    object Uninitialized : TotalBalanceState

    data class Error(val throwable: Throwable) : TotalBalanceState
    data class Success(
        val balance: String,
        val profileUrl: String
    ) : TotalBalanceState
}

sealed interface RecentTransactionState {
    object Loading : RecentTransactionState
    data class Error(val throwable: Throwable) : RecentTransactionState
    data class Success(val data: List<TransactionUiModel>) : RecentTransactionState
}
