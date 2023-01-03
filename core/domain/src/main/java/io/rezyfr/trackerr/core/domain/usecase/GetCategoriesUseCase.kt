package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.repository.CategoryRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val sessionManager: SessionManager,
) : BaseUseCaseFlow<Unit, List<CategoryModel>>(sessionManager) {
    override fun execute(param: Unit): Flow<List<CategoryModel>> {
        return repository.getCategories(sessionManager.uid).map {
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
