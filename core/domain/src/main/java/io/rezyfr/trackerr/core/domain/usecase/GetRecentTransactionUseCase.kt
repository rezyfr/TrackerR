package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.core.data.CategoryRepository
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.WalletRepository
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.asDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    private val firebaseAuth: FirebaseAuth
) : BaseUseCaseFlow<List<TransactionModel>>(firebaseAuth) {
    override fun execute(): Flow<List<TransactionModel>> {
        return repository.getRecentTransaction(firebaseAuth.uid).map {
            it.map { tf ->
                val category = categoryRepository.getCategoryByRef(tf.categoryRef?.id!!)
                val wallet = walletRepository.getWalletByRef(tf.walletRef?.id!!)
                tf.asDomainModel().copy(
                    category = category.name,
                    wallet = wallet.name
                )
            }
        }
    }
}
