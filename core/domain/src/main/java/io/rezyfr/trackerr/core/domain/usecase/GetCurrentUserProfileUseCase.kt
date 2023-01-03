package io.rezyfr.trackerr.core.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import io.rezyfr.trackerr.core.domain.model.UserModel
import io.rezyfr.trackerr.core.domain.repository.UserRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
) : BaseUseCaseFlow<Unit, Either<TrackerrError, UserModel>>(sessionManager) {
    override fun execute(param: Unit): Flow<Either<TrackerrError, UserModel>> {
        return userRepository.getCurrentUserProfile(sessionManager.uid).map {
            it.fold(
                {
                    it.left()
                },
                {
                    it.right()
                }
            )
        }
    }
}
