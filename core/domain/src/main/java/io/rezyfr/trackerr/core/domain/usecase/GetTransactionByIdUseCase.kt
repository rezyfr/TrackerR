package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.data.CategoryRepository
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.WalletRepository
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrDispatchers
import io.rezyfr.trackerr.core.data.session.SessionManager
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher,
    sessionManager: SessionManager
) : BaseUseCase<String, TransactionModel>(dispatcher, sessionManager) {
    override suspend fun execute(param: String): ResultState<TransactionModel> {
        val response = repository.getTransactionById(param).map {
            it.asDomainModel().copy(
                category = categoryRepository.getCategoryByRef(it.categoryRef?.id!!)
                    .asDomainModel(),
                wallet = walletRepository.getWalletByRef(it.walletRef?.id!!).asDomainModel()
            )
        }
        return if (response.isSuccess) ResultState.Success(response.getOrNull()!!)
        else ResultState.Error(response.exceptionOrNull())
    }
}
