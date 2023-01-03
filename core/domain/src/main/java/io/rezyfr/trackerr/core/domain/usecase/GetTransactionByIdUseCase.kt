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

class GetTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher,
    sessionManager: SessionManager
) : BaseUseCase<String, TransactionModel>(dispatcher, sessionManager) {
    override suspend fun execute(param: String): ResultState<TransactionModel> {
        val response = repository.getTransactionById(param)
        return if (response.isRight()) {
            val transaction = response.orNull()!!
            val category = categoryRepository.getCategoryByRef(transaction.categoryRef?.id!!)
            val wallet = walletRepository.getWalletByRef(transaction.walletRef?.id!!)
            if (category.isRight() && wallet.isRight()) {
                ResultState.Success(
                    transaction.copy(
                        category = category.orNull()!!,
                        wallet = wallet.orNull()!!
                    )
                )
            } else {
                ResultState.Error(Throwable(category.getLeft() ?: wallet.getLeft()))
            }
        } else {
            ResultState.Error(response.getLeft())
        }
    }
}
