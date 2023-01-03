

package io.rezyfr.trackerr.core.persistence.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.rezyfr.trackerr.core.persistence.datastore.DataStoreKeys
import io.rezyfr.trackerr.core.persistence.datastore.TrDataStore
import io.rezyfr.trackerr.core.persistence.room.AppDatabase
import io.rezyfr.trackerr.core.persistence.room.HomeScreenDao
import io.rezyfr.trackerr.core.persistence.source.DataStoreSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideHomeScreenDao(appDatabase: AppDatabase): HomeScreenDao {
        return appDatabase.homeScreenDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "HomeScreen"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataStoreSource(
        dataStore: TrDataStore,
        dataStoreKeys: DataStoreKeys
    ): DataStoreSource {
        return DataStoreSource(dataStore, dataStoreKeys)
    }
}
