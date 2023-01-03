package io.rezyfr.trackerr.core.domain.mapper

import arrow.core.Either
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

inline fun <reified T: Any, R> QuerySnapshot.toDomain(firestoreClass: Class<T>, mapper: (T) -> R): List<R> {
    return this.documents.map {
        val obj = it.toObject(firestoreClass)!!
        mapper(obj)
    }
}

inline fun <reified T: Any, R> DocumentSnapshot.toDomain(firestoreClass: Class<T>, mapper: (T) -> R): R {
    val obj = this.toObject(firestoreClass) ?: throw Exception("Cannot convert from ${firestoreClass.simpleName}, object is null")
    return mapper(obj)
}

fun <Error, Domain> Either<Error, Domain>.getLeft(): Error?{
    return this.swap().orNull()
}