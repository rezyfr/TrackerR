package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.data.CategoryRepository
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.WalletRepository
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrDispatchers
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
    firebaseAuth: FirebaseAuth,
) : BaseUseCaseFlow<String, Result<Nothing?>>(firebaseAuth) {

    override  fun execute(param: String): Flow<Result<Nothing?>> {
        return repository.deleteTransactionById(param)
    }
}
