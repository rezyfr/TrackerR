package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.domain.repository.IconRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetIconsUseCase @Inject constructor(
    private val repository: IconRepository,
    private val sessionManager: SessionManager,
) : BaseUseCaseFlow<Unit, List<String>>(sessionManager) {
    override fun execute(param: Unit): Flow<List<String>> {
        return repository.getIcons().map {
            it.fold(
                {
                    emptyList()
                },
                {
                    it
                }
            )
        }
    }
}
