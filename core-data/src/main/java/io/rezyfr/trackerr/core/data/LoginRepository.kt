package io.rezyfr.trackerr.core.data

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.CollectionReference
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrackerRDispatchers
import io.rezyfr.trackerr.core.data.model.UserModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

interface LoginRepository {
    fun storeUserData(google: GoogleSignInAccount): Flow<Result<Boolean>>
}

class LoginRepositoryImpl @Inject constructor(
    private val collectionReference: CollectionReference,
    @Dispatcher(TrackerRDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : LoginRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun storeUserData(google: GoogleSignInAccount): Flow<Result<Boolean>> = flow {
        val user = UserModel(
            google.email.orEmpty(),
            google.displayName.orEmpty()
        )
        emit(suspendCancellableCoroutine<Result<Boolean>> { continuation ->
            collectionReference.document(google.id.orEmpty()).set(user).addOnFailureListener {
                continuation.resume(Result.failure(it)) {
                }
            }.addOnSuccessListener {
                continuation.resume(Result.success(true)) {
                }
            }
        })
    }.flowOn(ioDispatcher)
}