package io.rezyfr.trackerr.core.domain.repository

import arrow.core.Either
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.WalletModel
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getTotalBalance(uid: String?): Flow<Either<TrackerrError, Long>>
    fun getWallets(uid: String?): Flow<Either<TrackerrError, List<WalletModel>>>
    suspend fun getWalletRefById(id: String): Either<TrackerrError, DocumentReference>
    suspend fun getWalletByRef(ref: String): Either<TrackerrError, WalletModel>
}
