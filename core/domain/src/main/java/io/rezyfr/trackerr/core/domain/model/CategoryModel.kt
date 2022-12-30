package io.rezyfr.trackerr.core.domain.model

import io.rezyfr.trackerr.core.data.model.CategoryFirestore


data class CategoryModel(
    val id: String,
    val type: String,
    val name: String,
    val userId: String,
) {
    companion object {
        fun emptyData() = CategoryModel(
            id = "",
            type = "",
            name = "",
            userId = ""
        )
    }
}

fun CategoryFirestore.asDomainModel() = CategoryModel(
    id, type, name, userId
)
