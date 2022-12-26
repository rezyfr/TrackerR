package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.UserRepository
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.asUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<TransactionModel>> {
        return repository.getRecentTransaction(userRepository.currentUserId).map {
            it.map { tf ->
                tf.asUiModel()
            }
        }
    }
}
