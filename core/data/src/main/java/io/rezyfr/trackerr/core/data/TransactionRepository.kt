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
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrDispatchers
import io.rezyfr.trackerr.core.data.model.TransactionFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface TransactionRepository {
    fun getRecentTransaction(uid: String?): Flow<List<TransactionFirestore>>
}

class TransactionRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : TransactionRepository {
    override fun getRecentTransaction(uid: String?): Flow<List<TransactionFirestore>> =
        callbackFlow {
            val callback =
                db.whereEqualTo("userId", uid)
                    .orderBy("date")
                    .limit(5)
                    .addSnapshotListener { value, error ->
                        val transactions = value?.map {
                            it?.toObject(TransactionFirestore::class.java)!!
                        }
                        if (transactions != null) {
                            trySend(transactions)
                        }
                    }
            awaitClose { callback.remove() }
        }.flowOn(dispatcher)
}
