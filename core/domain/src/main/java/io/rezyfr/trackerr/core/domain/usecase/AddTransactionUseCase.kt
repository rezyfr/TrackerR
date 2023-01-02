package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.data.CategoryRepository
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.WalletRepository
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrDispatchers
import io.rezyfr.trackerr.core.data.session.SessionManager
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val categoryRepository: CategoryRepository,
    private val sessionManager: SessionManager,
    @Dispatcher(TrDispatchers.IO) coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<TransactionModel, Nothing?>(coroutineDispatcher, sessionManager) {

    override suspend fun execute(param: TransactionModel): ResultState<Nothing?> {
        val walletRef = walletRepository.getWalletRefById(param.wallet.id)
        val categoryRef = categoryRepository.getCategoryRefById(param.category.id)
        val response = repository.saveTransaction(
            param.asAddTransactionFirestore(
                uid = sessionManager.uid,
                walletRef = walletRef,
                categoryRef = categoryRef,
                id = param.id
            )
        )
        return if (response.isSuccess) ResultState.Success(null)
        else ResultState.Error(response.exceptionOrNull())
    }
}
