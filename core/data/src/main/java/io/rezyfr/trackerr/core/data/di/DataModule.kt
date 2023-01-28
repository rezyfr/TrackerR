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
import io.rezyfr.trackerr.core.data.session.SessionManagerImpl
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.repository.*
import io.rezyfr.trackerr.core.domain.session.SessionManager
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
    ): SessionManager = SessionManagerImpl(dataStoreSource, firebaseAuth, context)

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
    fun provideUserRepository(
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

    @Singleton
    @Provides
    fun provideIconRepository(
        @Named("icon") collectionReference: CollectionReference,
        @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher
    ): IconRepository {
        return IconRepositoryImpl(collectionReference, dispatcher)
    }
}