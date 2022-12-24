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

package io.rezyfr.trackerr.core.data.di

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.TransactionRepositoryImpl
import io.rezyfr.trackerr.core.data.AuthRepository
import io.rezyfr.trackerr.core.data.AuthRepositoryImpl
import io.rezyfr.trackerr.core.data.model.TransactionModel
import io.rezyfr.trackerr.core.database.HomeScreenDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideHomeScreenRepository(
        @Named("transaction") collectionReference: CollectionReference,
        @Dispatcher(TrackerRDispatchers.IO) dispatcher: CoroutineDispatcher
    ): TransactionRepository {
        return TransactionRepositoryImpl(collectionReference, dispatcher)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(
        @Named("users") collectionReference: CollectionReference,
        @ApplicationContext appContext: Context,
        @Dispatcher(TrackerRDispatchers.IO) dispatcher: CoroutineDispatcher
    ) : AuthRepository {
        return AuthRepositoryImpl(collectionReference, appContext, dispatcher)
    }
}

class FakeTransactionRepository @Inject constructor() : TransactionRepository {
    override fun getRecentTransaction(): Flow<List<TransactionModel>> {
        return flow { listOf<TransactionModel>() }
    }
}
