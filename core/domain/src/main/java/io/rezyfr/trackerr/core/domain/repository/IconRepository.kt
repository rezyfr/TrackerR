package io.rezyfr.trackerr.core.domain.repository

import arrow.core.Either
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.WalletModel
import kotlinx.coroutines.flow.Flow

interface IconRepository {
    fun getIcons(): Flow<Either<TrackerrError, List<String>>>
    suspend fun getIconRefByUrl(url: String): Either<TrackerrError, DocumentReference>
    suspend fun getIconByRef(ref: String): Either<TrackerrError, String>

}
