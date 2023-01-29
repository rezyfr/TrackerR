package io.rezyfr.trackerr.core.data

import arrow.core.Either
import arrow.core.left
import arrow.core.leftWiden
import arrow.core.right
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.core.data.model.IconFirestore
import io.rezyfr.trackerr.core.data.model.WalletFirestore
import io.rezyfr.trackerr.core.data.model.asDomainModel
import io.rezyfr.trackerr.core.data.model.asString
import io.rezyfr.trackerr.core.data.util.cacheFirstSnapshot
import io.rezyfr.trackerr.core.data.util.withUserId
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.mapper.toDomain
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.toError
import io.rezyfr.trackerr.core.domain.repository.IconRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

class IconRepositoryImpl(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : IconRepository {
    override fun getIcons(): Flow<Either<TrackerrError, List<String>>> {
        return callbackFlow {
            val listener = db.cacheFirstSnapshot(
                onSuccess = {
                    trySend(it.toDomain(IconFirestore::class.java, IconFirestore::asString).right())
                },
                onError = {
                    trySend(it.toError().left())
                    close(it.toError())
                }
            )
            awaitClose { listener.asDeferred().cancel() }
        }.flowOn(dispatcher)
    }

    override suspend fun getIconRefByUrl(url: String): Either<TrackerrError, DocumentReference> {
        return suspendCancellableCoroutine { cont ->
            db.whereEqualTo("url", url).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result
                    if (result != null && !result.isEmpty) {
                        cont.resumeWith(Result.success(result.documents[0].reference.right()))
                    } else {
                        cont.resumeWith(Result.success(TrackerrError("Icon not found").left()))
                    }
                } else {
                    cont.resumeWith(Result.failure(it.exception ?: TrackerrError("Icon not found")))
                }
            }
        }
    }

    override suspend fun getIconByRef(ref: String): Either<TrackerrError, String> {
        val snapshot = db.document(ref).get().await()
        return snapshot
            .toDomain(IconFirestore::class.java, IconFirestore::asString).right()
            .leftWiden()
    }
}
