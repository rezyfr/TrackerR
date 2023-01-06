package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.mapper.getLeft
import io.rezyfr.trackerr.core.domain.repository.TransactionRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    sessionManager: SessionManager,
    @Dispatcher(TrDispatchers.IO) coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<String, Nothing?>(coroutineDispatcher, sessionManager) {

    override suspend fun execute(param: String): ResultState<Nothing?> {
        val result = repository.deleteTransactionById(param)
        return if (result.isRight()) ResultState.Success(null)
        else ResultState.Error(result.getLeft())
    }
}
