package io.rezyfr.trackerr.core.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.rezyfr.trackerr.core.data.CategoryRepository
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.asDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val firebaseAuth: FirebaseAuth
) : BaseUseCaseFlow<Unit, List<CategoryModel>>(firebaseAuth) {
    override fun execute(param: Unit): Flow<List<CategoryModel>> {
        return repository.getCategories(firebaseAuth.uid)
            .map {
                it.map { it.asDomainModel() }
            }
    }
}
