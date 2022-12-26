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
import io.rezyfr.trackerr.core.data.model.WalletFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface WalletRepository {
    fun getTotalBalance(userId: String): Flow<Long>
}

class WalletRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : WalletRepository {
    override fun getTotalBalance(userId: String): Flow<Long> = callbackFlow {
        val listener = db.whereEqualTo("userId", userId).addSnapshotListener { value, error ->
            val wallets = value?.map {
                it?.toObject(WalletFirestore::class.java)!!
            } ?: listOf()
            trySend(wallets.sumOf { it.balance })
        }
        awaitClose { listener.remove() }
    }.flowOn(dispatcher)
}
