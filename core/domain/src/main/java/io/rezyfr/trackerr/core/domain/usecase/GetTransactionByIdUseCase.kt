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

class GetTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    firebaseAuth: FirebaseAuth
) : BaseUseCaseFlow<String, TransactionModel>(firebaseAuth) {
    override fun execute(param: String): Flow<TransactionModel> {
        return repository.getTransactionById(param).map { tf ->
            val category = categoryRepository.getCategoryByRef(tf.categoryRef?.id!!)
            val wallet = walletRepository.getWalletByRef(tf.walletRef?.id!!)
            tf.asDomainModel().copy(
                category = category.asDomainModel(),
                wallet = wallet.asDomainModel()
            )
        }
    }
}
