package io.rezyfr.trackerr.core.domain

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val trackerRDispatchers: TrDispatchers)

enum class TrDispatchers {
    IO
}
