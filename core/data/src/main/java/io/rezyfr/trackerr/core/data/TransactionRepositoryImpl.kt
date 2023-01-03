package io.rezyfr.trackerr.core.data

import arrow.core.Either
import arrow.core.left
import arrow.core.leftWiden
import arrow.core.right
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.data.model.TransactionFirestore
import io.rezyfr.trackerr.core.data.model.asDomainModel
import io.rezyfr.trackerr.core.domain.mapper.toDomain
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.toError
import io.rezyfr.trackerr.core.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : TransactionRepository {
    override fun getRecentTransaction(uid: String?): Flow<Either<TrackerrError, List<TransactionModel>>> =
        callbackFlow {
            val callback =
                db.whereEqualTo("userId", uid)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(5)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            close(error.toError())
                        }
                        if (value != null) {
                            trySend(
                                value.toDomain(
                                    TransactionFirestore::class.java,
                                    TransactionFirestore::asDomainModel
                                ).right().leftWiden()
                            )
                        }
                    }
            awaitClose { callback.remove() }
        }.flowOn(dispatcher)

    override suspend fun getTransactionById(id: String): Either<TrackerrError, TransactionModel> {
        val trx = db.document(id).get().await()
        return try {
            checkNotNull(trx.toDomain(TransactionFirestore::class.java, TransactionFirestore::asDomainModel)).right().leftWiden()
        } catch (e: Exception) {
            e.toError().left()
        }
    }

    override fun deleteTransactionById(id: String): Flow<Either<TrackerrError, Nothing?>> {
        return callbackFlow {
            val callback =
                db.document(id).delete().addOnSuccessListener {
                    trySend(null.right().leftWiden())
                }.addOnFailureListener {
                    trySend(it.toError().left())
                }
            awaitClose { callback.asDeferred().cancel() }
        }.flowOn(dispatcher)
    }

    override suspend fun saveTransaction(
        transaction: HashMap<String, Any>
    ): Either<TrackerrError, Nothing?> {
        return try {
            if (transaction["id"] == null) {
                val doc = db.document()
                transaction["id"] = doc.id
                doc.set(transaction)
                db.add(doc)
                    .await()
            } else {
                db.document(transaction["id"] as String)
                    .set(transaction)
                    .await()
            }
            Either.Right(null)
        } catch (e: Exception) {
            if (e.message?.contains("DocumentReference") == true) Either.Right(null)
            else e.toError().left()
        }
    }
}
