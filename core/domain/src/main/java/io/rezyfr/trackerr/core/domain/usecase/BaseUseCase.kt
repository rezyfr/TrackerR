package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.common.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseUseCase<in P, out R> constructor(
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(param: P): ResultState<R> = try {
        if (firebaseAuth.currentUser == null) {
            error("403")
        }
        withContext(dispatcher) {
            execute(param)
        }
    } catch (e: Exception) {
        ResultState.Error(exception = e)
    }

    protected abstract suspend fun execute(param: P): ResultState<R>
}