package io.rezyfr.trackerr.core.data

import arrow.core.Either
import arrow.core.leftWiden
import arrow.core.right
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.data.model.WalletFirestore
import io.rezyfr.trackerr.core.data.model.asDomainModel
import io.rezyfr.trackerr.core.domain.mapper.toDomain
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.domain.model.toError
import io.rezyfr.trackerr.core.domain.repository.WalletRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : WalletRepository {
    override fun getTotalBalance(uid: String?): Flow<Either<TrackerrError, Long>> = callbackFlow {
        val listener = db.withUserId(uid).addSnapshotListener { value, error ->
            if (error != null) {
                close(error.toError())
            }
            if (value != null) {
                trySend(
                    value.toDomain(WalletFirestore::class.java, WalletFirestore::asDomainModel)
                        .sumOf { it.balance }.right()
                )
            }
        }
        awaitClose { listener.remove() }
    }.flowOn(dispatcher)

    override fun getWallets(uid: String?): Flow<Either<TrackerrError, List<WalletModel>>> {
        return callbackFlow {
            val listener = db.withUserId(uid).addSnapshotListener { value, error ->
                if (error != null) {
                    close(error.toError())
                }
                if (value != null) {
                    val wallet = value.documents.map {
                        it.toDomain(WalletFirestore::class.java, WalletFirestore::asDomainModel).copy(
                            id = it.id
                        )
                    }
                    trySend(wallet.right())
                }
            }
            awaitClose { listener.remove() }
        }.flowOn(dispatcher)
    }

    override suspend fun getWalletRefById(id: String): Either<TrackerrError, DocumentReference> {
        return db.document(id).get().await().reference
            .right()
            .leftWiden()
    }

    override suspend fun getWalletByRef(ref: String): Either<TrackerrError, WalletModel> {
        val snapshot = db.document(ref).get().await()
        return snapshot
            .toDomain(WalletFirestore::class.java, WalletFirestore::asDomainModel).copy(id = snapshot.id).right()
            .leftWiden()
    }
}
