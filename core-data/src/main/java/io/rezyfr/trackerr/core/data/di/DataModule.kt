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

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import io.rezyfr.trackerr.core.data.HomeScreenRepository
import io.rezyfr.trackerr.core.data.DefaultHomeScreenRepository
import io.rezyfr.trackerr.core.data.LoginRepository
import io.rezyfr.trackerr.core.data.LoginRepositoryImpl
import io.rezyfr.trackerr.core.database.HomeScreenDao
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideHomeScreenRepository(
        homeScreenDao: HomeScreenDao
    ): HomeScreenRepository {
        return DefaultHomeScreenRepository(homeScreenDao)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(
        @Named("users") collectionReference: CollectionReference,
        @Dispatcher(TrackerRDispatchers.IO) dispatcher: CoroutineDispatcher
    ) : LoginRepository {
        return LoginRepositoryImpl(collectionReference, dispatcher)
    }
}

class FakeHomeScreenRepository @Inject constructor() : HomeScreenRepository {
    override val homeScreens: Flow<List<String>> = flowOf(fakeHomeScreens)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }
}

val fakeHomeScreens = listOf("One", "Two", "Three")
