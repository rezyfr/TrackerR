package io.rezyfr.trackerr.core.domain.model

import io.rezyfr.trackerr.core.data.model.CategoryFirestore


data class CategoryModel(
    val id: String,
    val type: String,
    val name: String,
    val userId: String,
)

fun CategoryFirestore.asDomainModel() = CategoryModel(
    id, type, name, userId
)
