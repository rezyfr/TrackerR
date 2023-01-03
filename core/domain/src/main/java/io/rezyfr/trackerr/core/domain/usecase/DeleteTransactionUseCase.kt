package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.domain.repository.TransactionRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    sessionManager: SessionManager,
) : BaseUseCaseFlow<String, Result<Nothing?>>(sessionManager) {

    override fun execute(param: String): Flow<Result<Nothing?>> {
        return repository.deleteTransactionById(param).map {
            it.fold(
                {
                    Result.failure(it)
                },
                {
                    Result.success(null)
                }
            )
        }
    }
}
