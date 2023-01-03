package io.rezyfr.trackerr.core.domain.usecase

import arrow.core.getOrElse
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.domain.mapper.getLeft
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.repository.CategoryRepository
import io.rezyfr.trackerr.core.domain.repository.TransactionRepository
import io.rezyfr.trackerr.core.domain.repository.WalletRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    private val sessionManager: SessionManager
) : BaseUseCaseFlow<Unit, ResultState<List<TransactionModel>>>(sessionManager) {
    override fun execute(param: Unit): Flow<ResultState<List<TransactionModel>>> {
        return repository.getRecentTransaction(sessionManager.uid).map {
            if (it.isRight()) {
                ResultState.Success(it.getOrElse { listOf() }.map { tf ->
                    val category = categoryRepository.getCategoryByRef(tf.categoryRef!!.id)
                    val wallet = walletRepository.getWalletByRef(tf.walletRef!!.id)
                    if (category.isRight() && wallet.isRight()) {
                        tf.copy(
                            category = category.orNull()!!,
                            wallet = wallet.orNull()!!
                        )
                    } else {
                        throw Throwable(category.getLeft() ?: wallet.getLeft())
                    }
                })
            } else {
                throw Throwable(it.getLeft())
            }
        }.catch {
            ResultState.Error(Throwable(it.message))
        }
    }
}
