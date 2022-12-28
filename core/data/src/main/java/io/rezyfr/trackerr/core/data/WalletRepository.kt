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
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrDispatchers
import io.rezyfr.trackerr.core.data.model.WalletFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface WalletRepository {
    fun getTotalBalance(uid: String?): Flow<Long>
    fun getWallets(uid: String?): Flow<List<WalletFirestore>>
    suspend fun getWalletRefById(id: String): DocumentReference
    suspend fun getWalletByRef(ref: String): WalletFirestore
}

class WalletRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : WalletRepository {
    override fun getTotalBalance(uid: String?): Flow<Long> = callbackFlow {
        val listener = db.whereEqualTo("userId", uid).addSnapshotListener { value, error ->
            val balanceResponse = value?.map {
                it?.toObject(WalletFirestore::class.java)!!
            }?.sumOf { it.balance } ?: throw Throwable(error)
            trySend(balanceResponse)
        }
        awaitClose { listener.remove() }
    }.flowOn(dispatcher)

    override fun getWallets(uid: String?): Flow<List<WalletFirestore>> {
        return callbackFlow {
            val listener = db.whereEqualTo("userId", uid).addSnapshotListener { value, error ->
                val walletResponse =
                    value?.toObjects(WalletFirestore::class.java) ?: throw Throwable(error)
                trySend(walletResponse)
            }
            awaitClose { listener.remove() }
        }.flowOn(dispatcher)
    }

    override suspend fun getWalletRefById(id: String): DocumentReference {
        return db.document(id).get().await().reference
    }

    override suspend fun getWalletByRef(ref: String): WalletFirestore {
        return db.document(ref).get().await().toObject(WalletFirestore::class.java) ?: throw Throwable()
    }
}
