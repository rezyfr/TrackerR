package io.rezyfr.trackerr.core.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import io.rezyfr.trackerr.core.data.di.Dispatcher
import io.rezyfr.trackerr.core.data.di.TrDispatchers
import io.rezyfr.trackerr.core.data.model.UserFirestore
import io.rezyfr.trackerr.core.persistence.source.DataStoreSource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserRepository {
    val isLoggedIn: Flow<Boolean>
    fun getCurrentUserProfile(uid: String): Flow<UserFirestore>
    suspend fun storeUserData(google: GoogleSignInAccount, auth: AuthResult): Result<Boolean>
    suspend fun signInFirebase(gsa: GoogleSignInAccount): Task<AuthResult>
}

class UserRepositoryImpl @Inject constructor(
    private val collectionReference: CollectionReference,
    private val firebaseAuth: FirebaseAuth,
    @Dispatcher(TrDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {
    override val isLoggedIn: Flow<Boolean>
        get() = callbackFlow {
            val listener = Firebase.auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
                trySend(it.isSuccessful)
            }
            awaitClose { listener?.asDeferred()?.cancel() }
        }

    override fun getCurrentUserProfile(uid: String): Flow<UserFirestore> = callbackFlow {
        val listener = collectionReference.document(uid).get().addOnCompleteListener {
            trySend(
                it.result.toObject(UserFirestore::class.java)
                    ?: throw IllegalStateException("User not found")
            )
        }
        awaitClose { listener.asDeferred().cancel() }
    }.flowOn(ioDispatcher)

    override suspend fun storeUserData(
        google: GoogleSignInAccount,
        auth: AuthResult
    ): Result<Boolean> {
        return try {
            val user = UserFirestore(
                id = auth.user?.uid ?: google.idToken.orEmpty(),
                name = google.displayName.orEmpty(),
                email = google.email.orEmpty(),
                photoUrl = google.photoUrl.toString()
            )
            collectionReference.document(user.id).set(user).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun signInFirebase(gsa: GoogleSignInAccount): Task<AuthResult> =
        suspendCancellableCoroutine { cont ->
            val token = gsa.idToken.orEmpty()
            val firebaseCred = GoogleAuthProvider.getCredential(token, null)
            firebaseAuth.signInWithCredential(firebaseCred).addOnCompleteListener { auth ->
                cont.resume(auth) {
                    cont.cancel()
                }
            }
        }
}