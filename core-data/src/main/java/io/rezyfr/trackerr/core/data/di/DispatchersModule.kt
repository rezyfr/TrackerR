package io.rezyfr.trackerr.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(TrackerRDispatchers.IO)
    @Singleton
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}