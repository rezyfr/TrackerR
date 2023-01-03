package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName
import io.rezyfr.trackerr.core.domain.model.CategoryModel

data class CategoryFirestore(
    @get:PropertyName("id")
    val id: String = "",
    @get:PropertyName("type")
    val type: String = "",
    @get:PropertyName("name")
    val name: String = "",
    @get:PropertyName("userId")
    val userId: String = "",
)
fun CategoryFirestore.asDomainModel() = CategoryModel(
    id, type, name, userId
)
