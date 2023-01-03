package io.rezyfr.trackerr.core.domain.repository

import arrow.core.Either
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getRecentTransaction(uid: String?): Flow<Either<TrackerrError, List<TransactionModel>>>
    suspend fun getTransactionById(id: String): Either<TrackerrError, TransactionModel>

    fun deleteTransactionById(id: String): Flow<Either<TrackerrError, Nothing?>>
    suspend fun saveTransaction(transaction: HashMap<String, Any>): Either<TrackerrError, Nothing?>
}
