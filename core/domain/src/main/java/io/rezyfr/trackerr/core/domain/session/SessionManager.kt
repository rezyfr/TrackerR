package io.rezyfr.trackerr.core.domain.session

import kotlinx.coroutines.flow.Flow

interface SessionManager {
    val uid: String
    fun isLoggedIn(): Flow<Boolean>
    fun logout(): Flow<Result<Boolean>>
}
