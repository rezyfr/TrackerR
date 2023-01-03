package io.rezyfr.trackerr.core.domain.usecase

import arrow.core.getOrElse
import io.rezyfr.trackerr.core.domain.repository.WalletRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTotalBalanceUseCase @Inject constructor(
    private val repository: WalletRepository,
    private val sessionManager: SessionManager
) : BaseUseCaseFlow<Unit, Long>(sessionManager) {
    override fun execute(param: Unit): Flow<Long> {
        return repository.getTotalBalance(sessionManager.uid).map {
            it.getOrElse { 0L }
        }
    }
}
