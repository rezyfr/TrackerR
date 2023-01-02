/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rezyfr.trackerr.core.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrDispatchers
import io.rezyfr.trackerr.core.data.model.AddTransactionFirestore
import io.rezyfr.trackerr.core.data.model.TransactionFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TransactionRepository {
    fun getRecentTransaction(uid: String?): Flow<List<TransactionFirestore>>
    suspend fun getTransactionById(id: String): Result<TransactionFirestore>

    fun deleteTransactionById(id: String): Flow<Result<Nothing?>>
    suspend fun saveTransaction(
        transaction: AddTransactionFirestore
    ): Result<Nothing?>
}

class TransactionRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : TransactionRepository {
    override fun getRecentTransaction(uid: String?): Flow<List<TransactionFirestore>> =
        callbackFlow {
            val callback =
                db.whereEqualTo("userId", uid)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(5)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        val transactions = value?.toObjects(TransactionFirestore::class.java)
                        if (transactions != null) {
                            trySend(transactions)
                        }
                    }
            awaitClose { callback.remove() }
        }.flowOn(dispatcher)

    override suspend fun getTransactionById(id: String): Result<TransactionFirestore> {
        val trx = db.document(id).get().await()
        return try {
            Result.success(checkNotNull(trx.toObject(TransactionFirestore::class.java)))
        } catch (e: Exception) {
            Result.failure<TransactionFirestore>(e)
        }
    }

    override fun deleteTransactionById(id: String): Flow<Result<Nothing?>> {
        return callbackFlow {
            val callback =
                db.document(id).delete().addOnSuccessListener {
                    trySend(Result.success(null))
                }.addOnFailureListener {
                    trySend(Result.failure<Nothing>(it))
                }
            awaitClose { callback.asDeferred().cancel() }
        }.flowOn(dispatcher)
    }

    override suspend fun saveTransaction(
        transaction: AddTransactionFirestore
    ): Result<Nothing?> {
        return try {
            if (transaction.id.isNullOrEmpty()) {
                val doc = db.document()
                doc.set(transaction.copy(id = doc.id))
                db.add(doc)
                    .await()
            } else {
                db.document(transaction.id)
                    .set(transaction)
                    .await()
            }
            Result.success(null)
        } catch (e: Exception) {
            if (e.message?.contains("DocumentReference") == true) Result.success(null)
            else Result.failure(e)
        }
    }
}
