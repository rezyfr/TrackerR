package io.rezyfr.trackerr.core.data

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrackerRDispatchers
import io.rezyfr.trackerr.core.data.model.UserModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>
    fun storeUserData(google: GoogleSignInAccount): Flow<Result<Boolean>>
    fun logout(): Flow<Result<Boolean>>
}

class AuthRepositoryImpl @Inject constructor(
    private val collectionReference: CollectionReference,
    private val context: Context,
    @Dispatcher(TrackerRDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {
    override val isLoggedIn: Flow<Boolean>
        get() = callbackFlow {
            val listener = Firebase.auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
                trySend(it.isSuccessful)
            }
            awaitClose { listener?.asDeferred()?.cancel() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun storeUserData(google: GoogleSignInAccount): Flow<Result<Boolean>> = flow {
        val token = google.idToken.orEmpty()
        val firebaseCred = GoogleAuthProvider.getCredential(token, null)
        emit(suspendCancellableCoroutine<Result<Boolean>> { continuation ->
            Firebase.auth.signInWithCredential(firebaseCred).addOnCompleteListener { auth ->
                if (auth.isSuccessful) {
                    val user = UserModel(
                        id = auth.result?.user?.uid ?: google.idToken.orEmpty(),
                        name = google.displayName.orEmpty(),
                        email = google.email.orEmpty(),
                        photoUrl = google.photoUrl.toString()
                    )
                    collectionReference.document(user.id).set(user).addOnCompleteListener { task ->
                        task.addOnSuccessListener { _ ->
                            continuation.resume(Result.success(true)) {
                                continuation.cancel()
                            }
                        }
                        task.addOnFailureListener { _ ->
                            continuation.resume(Result.success(true)) {
                                continuation.cancel()
                            }
                        }
                    }
                } else {
                    continuation.resume(Result.failure(auth.exception!!)) {
                        continuation.cancel()
                    }
                }
            }
        })
    }.flowOn(ioDispatcher)

    override fun logout(): Flow<Result<Boolean>> {
        return callbackFlow {
            val listener = Identity.getSignInClient(context).signOut().addOnCompleteListener {
                if (it.isSuccessful) {
                    Firebase.auth.signOut()
                    trySend(Result.success(true))
                } else {
                    trySend(Result.failure(it.exception ?: Exception("Unknown error")))
                }
            }
            trySend(Result.failure(Exception("Unknown error")))
            awaitClose { listener.asDeferred().cancel() }
        }
    }
}