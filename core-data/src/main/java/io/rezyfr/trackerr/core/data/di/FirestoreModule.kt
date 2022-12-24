package io.rezyfr.trackerr.core.data.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirestoreModule {

    @Provides
    @Singleton
    fun provideFirebaseDB() : FirebaseFirestore {
      return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    @Named("users")
    fun provideUserCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection("users")
    }

    @Provides
    @Singleton
    @Named("transaction")
    fun provideTransactionCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection("transaction")
    }
}