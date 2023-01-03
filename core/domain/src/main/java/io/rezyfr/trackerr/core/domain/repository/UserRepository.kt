package io.rezyfr.trackerr.core.domain.repository

import arrow.core.Either
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val isLoggedIn: Flow<Boolean>
    fun getCurrentUserProfile(uid: String): Flow<Either<TrackerrError, UserModel>>
    suspend fun storeUserData(google: GoogleSignInAccount, auth: AuthResult): Result<Boolean>
    suspend fun signInFirebase(gsa: GoogleSignInAccount): Task<AuthResult>
}
