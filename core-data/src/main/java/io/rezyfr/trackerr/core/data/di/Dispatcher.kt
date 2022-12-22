package io.rezyfr.trackerr.core.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val trackerRDispatchers: TrackerRDispatchers)

enum class TrackerRDispatchers {
    IO
}
