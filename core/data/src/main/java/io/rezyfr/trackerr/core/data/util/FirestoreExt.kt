package io.rezyfr.trackerr.core.data.util

import arrow.core.Either
import arrow.core.left
import arrow.core.leftWiden
import arrow.core.right
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.mapper.toDomain
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.toError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

fun CollectionReference.withUserId(uid: String) = this.whereEqualTo("userId", uid)

fun Query.cacheFirstSnapshot(
    onSuccess: (value: QuerySnapshot) -> Unit,
    onError: (error: Exception) -> Unit
) = getFromCache(
    onSuccess = {
        if (it.isEmpty) {
            getFromServer(onSuccess, onError)
        } else {
            onSuccess(it)
        }
    },
    onError = {
        getFromServer(onSuccess, onError)
    }
)

private fun Query.getFromCache(
    onSuccess: (value: QuerySnapshot) -> Unit,
    onError: (error: Exception) -> Unit
) = this.get(Source.CACHE).handleResult(onSuccess, onError)

private fun Query.getFromServer(
    onSuccess: (value: QuerySnapshot) -> Unit,
    onError: (error: Exception) -> Unit
) = this.get(Source.SERVER).handleResult(onSuccess, onError)

private fun <T> Task<T>.handleResult(
    onSuccess: (value: T) -> Unit,
    onError: (error: Exception) -> Unit
) =
    this.addOnCompleteListener {
        if (it.isSuccessful) {
            onSuccess(it.result)
        } else {
            onError(it.exception ?: Exception("Unknown error"))
        }
    }

/**
 * OnFalseException is used to handle error on while submitting
 * a document that has DocumentReference as the type of any field
 */

suspend fun <T> Task<T>.handleNullAwait(
    onFalseException: ((error: Exception) -> Boolean)? = null,
): Either<TrackerrError, Nothing?> {
    return try {
        this.await()
        Either.Right(null)
    } catch (e: Exception) {
        if (onFalseException?.invoke(e) == true) {
            return Either.Right(null)
        }
        e.toError().left()
    }
}

suspend inline fun <reified F : Any, R> Task<DocumentSnapshot>.handleDocumentSnapshot(
    firestoreClass: Class<F>,
    snapshotMapper: (F) -> R,
) = handleAwait(
    firestoreClass = firestoreClass,
    snapshotMapper = snapshotMapper,
    snapshotClass = DocumentSnapshot::class.java
)

suspend inline fun <reified F : Any, R> Task<DocumentReference>.handleDocumentReference(
    firestoreClass: Class<F>,
    snapshotMapper: (F) -> R,
) = handleAwait(
    firestoreClass = firestoreClass,
    snapshotMapper = snapshotMapper,
    snapshotClass = DocumentReference::class.java
)

suspend inline fun <T, R, reified F : Any> Task<T>.handleAwait(
    firestoreClass: Class<F>,
    snapshotClass: Class<T>,
    snapshotMapper: (F) -> R,
): Either<TrackerrError, R> = try {
    val result = this.await()
    when {
        snapshotClass.isAssignableFrom(DocumentSnapshot::class.java) -> {
            val mappedResult =
                checkNotNull((result as DocumentSnapshot).toDomain(firestoreClass, snapshotMapper))
            mappedResult.right().leftWiden()
        }
        snapshotClass.isAssignableFrom(DocumentReference::class.java) -> {
            checkNotNull(
                (result as DocumentReference).get().result?.toDomain(
                    firestoreClass,
                    snapshotMapper
                )
            ).right().leftWiden()
        }
        else -> {
            throw Exception("Unknown snapshot class")
        }
    }
} catch (e: Exception) {
    Either.Left(e.toError())
}

suspend inline fun <T, R, reified F : Any> Task<T>.handleQuerySnapshot(
    firestoreClass: Class<F>,
    snapshotMapper: (F) -> R,
): Either<TrackerrError, List<R>> = try {
    val result = this.await()
    checkNotNull((result as QuerySnapshot).toDomain(firestoreClass, snapshotMapper)).right().leftWiden()
} catch (e: Exception) {
    Either.Left(e.toError())
}
