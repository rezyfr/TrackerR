package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.data.UserRepository
import io.rezyfr.trackerr.core.data.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalBalanceUseCase @Inject constructor(
    private val repository: WalletRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Long> {
        return repository.getTotalBalance(userRepository.currentFirebaseUser.uid)
    }
}
