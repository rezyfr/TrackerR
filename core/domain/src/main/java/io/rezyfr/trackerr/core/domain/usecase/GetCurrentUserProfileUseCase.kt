package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.data.UserRepository
import io.rezyfr.trackerr.core.data.model.UserFirestore
import io.rezyfr.trackerr.core.domain.model.UserModel
import io.rezyfr.trackerr.core.domain.model.asUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserModel> {
        return userRepository.getCurrentUserProfile(userRepository.currentFirebaseUser.uid).map {
            it.asUiModel()
        }
    }
}
