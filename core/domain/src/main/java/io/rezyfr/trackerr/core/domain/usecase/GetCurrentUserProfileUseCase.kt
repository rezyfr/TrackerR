package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.core.data.UserRepository
import io.rezyfr.trackerr.core.domain.model.UserModel
import io.rezyfr.trackerr.core.domain.model.asDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : BaseUseCaseFlow<UserModel>(firebaseAuth) {
    override fun execute(): Flow<UserModel> {
        return userRepository.getCurrentUserProfile(firebaseAuth.currentUser?.uid.orEmpty()).map {
            it.asDomainModel()
        }
    }
}
