package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.data.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

abstract class BaseUseCase<in P, out R> constructor(
    private val dispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(param: P): ResultState<R> = try {
        withContext(dispatcher) {
            if (sessionManager.isLoggedIn().firstOrNull() != true) {
                sessionManager.logout()
            }
            execute(param)
        }
    } catch (e: Exception) {
        ResultState.Error(exception = e)
    }

    protected abstract suspend fun execute(param: P): ResultState<R>
}