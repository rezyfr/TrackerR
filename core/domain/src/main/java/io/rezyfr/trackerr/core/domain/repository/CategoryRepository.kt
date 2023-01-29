package io.rezyfr.trackerr.core.domain.repository

import arrow.core.Either
import com.google.firebase.firestore.DocumentReference
import io.rezyfr.trackerr.core.domain.DomainResult
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.TrackerrError
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(uid: String?): Flow<Either<TrackerrError, List<CategoryModel>>>
    suspend fun getCategoryRefById(id: String): DomainResult<DocumentReference>
    suspend fun getCategoryByRef(ref: String): DomainResult<CategoryModel>
    suspend fun saveCategory(category: HashMap<String, Any>): Either<TrackerrError, Nothing?>

}
