package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.DocumentReference
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
    @get:PropertyName("color")
    val color: Int = 0,
    @get:PropertyName("icon")
    val icon: String = "",
    @get:PropertyName("iconRef")
    val iconRef: DocumentReference? = null,
)
fun CategoryFirestore.asDomainModel() = CategoryModel(
    id, type, name, userId, color, icon, iconRef
)
