package io.rezyfr.trackerr.core.data.session

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rezyfr.trackerr.core.domain.session.SessionManager
import io.rezyfr.trackerr.core.persistence.source.DataStoreSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val dataStore: DataStoreSource,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : SessionManager {
    override val uid: String = firebaseAuth.uid.orEmpty() ?: throw IllegalStateException("User is not logged in")

    override fun isLoggedIn(): Flow<Boolean> {
        return flowOf(firebaseAuth.currentUser != null && firebaseAuth.uid != null)
    }

    override fun logout(): Flow<Result<Boolean>> {
        return callbackFlow {
            val listener = Identity.getSignInClient(context).signOut()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        launch { dataStore.doLogout() }
                        firebaseAuth.signOut()
                        trySend(Result.success(true))
                    } else {
                        trySend(Result.failure(it.exception ?: Exception("Unknown error")))
                    }
                }
            awaitClose { listener.asDeferred().cancel() }
        }
    }
}
