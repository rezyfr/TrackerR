package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.core.data.WalletRepository
import io.rezyfr.trackerr.core.domain.model.WalletModel
import io.rezyfr.trackerr.core.domain.model.asDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWalletsUseCase @Inject constructor(
    private val repository: WalletRepository,
    private val firebaseAuth: FirebaseAuth
) : BaseUseCaseFlow<Unit, List<WalletModel>>(firebaseAuth) {
    override fun execute(param: Unit): Flow<List<WalletModel>> {
        return repository.getWallets(firebaseAuth.uid)
            .map {
                it.map { it.asDomainModel() }
            }
    }
}
