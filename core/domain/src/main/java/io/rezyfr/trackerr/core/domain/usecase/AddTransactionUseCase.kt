package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.mapper.getLeft
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.repository.CategoryRepository
import io.rezyfr.trackerr.core.domain.repository.TransactionRepository
import io.rezyfr.trackerr.core.domain.repository.WalletRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
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
        if (categoryRef.isRight() && walletRef.isRight()) {
            val response = repository.saveTransaction(
                param.asAddTransactionFirestore(
                    uid = sessionManager.uid,
                    walletRef = walletRef.orNull()!!,
                    categoryRef = categoryRef.orNull()!!,
                    id = param.id
                )
            )
            return if (response.isRight()) ResultState.Success(null)
            else ResultState.Error(response.getLeft()!!)
        }

        if (walletRef.isLeft()) return ResultState.Error(walletRef.getLeft()!!)
        if (categoryRef.isLeft()) return ResultState.Error(categoryRef.getLeft()!!)
        return ResultState.Error(RuntimeException("Unknown error"))
    }
}
