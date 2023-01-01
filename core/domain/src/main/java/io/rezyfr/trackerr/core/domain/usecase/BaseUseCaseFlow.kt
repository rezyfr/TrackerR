package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.data.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull

abstract class BaseUseCaseFlow<in P, out T> constructor(
    private val sessionManager: SessionManager,
) {
    operator fun invoke(param: P): Flow<T> {
        return execute(param)
            .catch {
                if (sessionManager.isLoggedIn().firstOrNull() != true) {
                    sessionManager.logout()
                }
                error(it.message ?: "Unknown error")
            }
    }

    protected abstract fun execute(param: P): Flow<T>
}