package io.rezyfr.trackerr.core.domain.usecase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.rezyfr.trackerr.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(gsa: GoogleSignInAccount): Flow<Result<Boolean>> = channelFlow {
        val firebaseSignIn = repo.signInFirebase(gsa)
        if (firebaseSignIn.isSuccessful) {
            val storeUserData = repo.storeUserData(gsa, firebaseSignIn.result)
            if (storeUserData.isSuccess) {
                trySend(Result.success(true))
            } else {
                trySend(Result.failure<Boolean>(storeUserData.exceptionOrNull() ?: Exception("Unknown error")))
            }
        } else {
            trySend(Result.failure<Boolean>(firebaseSignIn.exception ?: Exception("Unknown error")))
        }
    }.catch {
        Result.failure<Boolean>(Throwable(it.message ?: "Unknown error"))
    }
}