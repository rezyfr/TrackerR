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
import io.rezyfr.trackerr.core.data.model.CategoryFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CategoryRepository {
    fun getCategories(uid: String?): Flow<List<CategoryFirestore>>
    suspend fun getCategoryRefById(id: String): DocumentReference
    suspend fun getCategoryByRef(ref: String): CategoryFirestore
}

class CategoryRepositoryImpl @Inject constructor(
    private val db: CollectionReference,
    @Dispatcher(TrDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : CategoryRepository {

    override fun getCategories(uid: String?): Flow<List<CategoryFirestore>> {
        return callbackFlow {
            val listener = db.whereEqualTo("userId", uid).addSnapshotListener { value, error ->
                val categoriesResponse = value?.documents?.map {
                    val objectWithoutId = it.toObject(CategoryFirestore::class.java) ?: throw Throwable(error)
                    objectWithoutId.copy(id = it.id)
                } ?: listOf()
                trySend(categoriesResponse)
            }
            awaitClose { listener.remove() }
        }.flowOn(dispatcher)
    }

    override suspend fun getCategoryRefById(id: String): DocumentReference {
        return db.document(id).get().await().reference
    }

    override suspend fun getCategoryByRef(ref: String): CategoryFirestore {
        return db.document(ref).get().await().toObject(CategoryFirestore::class.java) ?: throw Throwable()
    }
}