package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.mapper.asResultState
import io.rezyfr.trackerr.core.domain.model.UserModel
import io.rezyfr.trackerr.core.domain.repository.UserRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCurrentUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    @Dispatcher(TrDispatchers.IO) dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, UserModel>( dispatcher, sessionManager) {
    override suspend fun execute(param: Unit): ResultState<UserModel> {
        return userRepository.getCurrentUserProfile(sessionManager.uid).asResultState()
    }
}
