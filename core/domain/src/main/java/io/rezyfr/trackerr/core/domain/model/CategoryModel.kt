package io.rezyfr.trackerr.core.domain.model


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
