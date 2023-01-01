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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.rezyfr.trackerr.core.data.*
import io.rezyfr.trackerr.core.data.session.SessionManager
import io.rezyfr.trackerr.core.persistence.source.DataStoreSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth,
        dataStoreSource: DataStoreSource,
    ) = SessionManager(dataStoreSource, firebaseAuth, context)

    @Singleton
    @Provides
    fun provideHomeScreenRepository(
        @Named("transaction") collectionReference: CollectionReference,
        @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher
    ): TransactionRepository {
        return TransactionRepositoryImpl(collectionReference, dispatcher)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(
        @Named("users") collectionReference: CollectionReference,
        firebaseAuth: FirebaseAuth,
        @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher,
    ): UserRepository {
        return UserRepositoryImpl(collectionReference, firebaseAuth, dispatcher)
    }

    @Singleton
    @Provides
    fun provideWalletRepository(
        @Named("wallet") collectionReference: CollectionReference,
        @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher
    ): WalletRepository {
        return WalletRepositoryImpl(collectionReference, dispatcher)
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(
        @Named("category") collectionReference: CollectionReference,
        @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher
    ): CategoryRepository {
        return CategoryRepositoryImpl(collectionReference, dispatcher)
    }
}