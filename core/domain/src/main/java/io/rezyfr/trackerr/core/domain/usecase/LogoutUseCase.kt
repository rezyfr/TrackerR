package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.data.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val session: SessionManager,
) : BaseUseCaseFlow<Unit, Boolean>(session) {
    override fun execute(param: Unit): Flow<Boolean> = session.logout().map { it.isSuccess }
}