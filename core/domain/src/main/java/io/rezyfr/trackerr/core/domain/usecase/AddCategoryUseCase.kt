package io.rezyfr.trackerr.core.domain.usecase

import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.domain.Dispatcher
import io.rezyfr.trackerr.core.domain.TrDispatchers
import io.rezyfr.trackerr.core.domain.mapper.getLeft
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.repository.CategoryRepository
import io.rezyfr.trackerr.core.domain.repository.IconRepository
import io.rezyfr.trackerr.core.domain.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val sessionManager: SessionManager,
    private val iconRepository: IconRepository,
    @Dispatcher(TrDispatchers.IO) coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<CategoryModel, Nothing?>(coroutineDispatcher, sessionManager) {

    override suspend fun execute(param: CategoryModel): ResultState<Nothing?> {
        val iconRef = iconRepository.getIconRefByUrl(param.icon)
        if (iconRef.isRight()) {
            val response = repository.saveCategory(
                param.asAddCategoryFirestore(
                    uid = sessionManager.uid,
                    iconRef = iconRef.orNull()!!,
                    id = param.id
                )
            )
            return if (response.isRight()) ResultState.Success(null)
            else ResultState.Error(response.getLeft()!!)
        }
        if (iconRef.isLeft()) return ResultState.Error(iconRef.getLeft()!!)
        return ResultState.Error(RuntimeException("Unknown error"))
    }
}
