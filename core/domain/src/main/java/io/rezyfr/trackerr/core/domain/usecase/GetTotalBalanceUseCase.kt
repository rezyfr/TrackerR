package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.asResult
import io.rezyfr.trackerr.core.data.WalletRepository
import io.rezyfr.trackerr.core.data.session.SessionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalBalanceUseCase @Inject constructor(
    private val repository: WalletRepository,
    private val sessionManager: SessionManager
) : BaseUseCaseFlow<Unit, Long>(sessionManager) {
    override fun execute(param: Unit): Flow<Long> {
        return repository.getTotalBalance(sessionManager.uid)
    }
}
