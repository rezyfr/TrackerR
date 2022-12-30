package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

abstract class BaseUseCaseFlow<in P, out T> constructor(
    private val firebaseAuth: FirebaseAuth
) {
    operator fun invoke(param: P): Flow<T> {
        return execute(param)
            .catch {
                if (firebaseAuth.currentUser == null) {
                    error("403")
                }
                error(it.message ?: "Unknown error")
            }
    }

    protected abstract fun execute(param: P): Flow<T>
}