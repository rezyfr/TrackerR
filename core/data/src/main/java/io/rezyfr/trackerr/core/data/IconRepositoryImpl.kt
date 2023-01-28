package io.rezyfr.trackerr.core.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.firebase.firestore.CollectionReference
import io.rezyfr.trackerr.core.data.model.IconFirestore
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
import kotlinx.coroutines.tasks.asDeferred

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
}
