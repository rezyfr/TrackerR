package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.data.CategoryRepository
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.WalletRepository
import io.rezyfr.trackerr.core.data.session.SessionManager
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.asDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    private val sessionManager: SessionManager
) : BaseUseCaseFlow<Unit, List<TransactionModel>>(sessionManager) {
    override fun execute(param: Unit): Flow<List<TransactionModel>> {
        return repository.getRecentTransaction(sessionManager.uid).map {
            it.map { tf ->
                val category = categoryRepository.getCategoryByRef(tf.categoryRef?.id!!)
                val wallet = walletRepository.getWalletByRef(tf.walletRef?.id!!)
                tf.asDomainModel().copy(
                    category = category.asDomainModel(),
                    wallet = wallet.asDomainModel()
                )
            }
        }
    }
}
