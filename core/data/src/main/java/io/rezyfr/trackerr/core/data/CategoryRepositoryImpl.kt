package io.rezyfr.trackerr.core.data

import arrow.core.Either
import arrow.core.left
import arrow.core.leftWiden
import arrow.core.right
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.core.data.model.CategoryFirestore
import io.rezyfr.trackerr.core.data.model.asDomainModel
import io.rezyfr.trackerr.core.data.util.cacheFirstSnapshot
import io.rezyfr.trackerr.core.data.util.handleAwait
import io.rezyfr.trackerr.core.data.util.handleDocumentSnapshot
import io.rezyfr.trackerr.core.data.util.withUserId
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.DomainResult
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.mapper.toDomain
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.toError
import io.rezyfr.trackerr.core.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : CategoryRepository {

    override fun getCategories(uid: String?): Flow<Either<TrackerrError, List<CategoryModel>>> {
        return callbackFlow {
            val listener = db.withUserId(uid.orEmpty())
                .cacheFirstSnapshot(
                    onSuccess = {
                        val categories = it.documents.map { doc ->
                            doc.toDomain(
                                CategoryFirestore::class.java,
                                CategoryFirestore::asDomainModel
                            )
                                .copy(id = doc.id)
                        }
                        trySend(categories.right())
                    },
                    onError = {
                        trySend(it.toError().left())
                        close(it)
                    }
                )
            awaitClose { listener.asDeferred().cancel() }
        }.catch {
            emit(TrackerrError(it.message.orEmpty()).left())
        }.flowOn(dispatcher)
    }

    override suspend fun getCategoryRefById(id: String): DomainResult<DocumentReference> {
        return db.document(id).get().await().reference
            .right()
            .leftWiden()
    }

    override suspend fun getCategoryByRef(ref: String): DomainResult<CategoryModel> {
        return db.document(ref).get().handleDocumentSnapshot(
            firestoreClass = CategoryFirestore::class.java,
            snapshotMapper = CategoryFirestore::asDomainModel
        )
    }
}
