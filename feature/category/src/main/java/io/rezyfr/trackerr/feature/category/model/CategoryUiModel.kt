package io.rezyfr.trackerr.feature.category.model

data class CategoryUiModel(
    val url: String,
    val name: String,
    val type: String
) {
    companion object {
        fun emptyData() = CategoryUiModel(
            url = "",
            name = "",
            type = ""
        )
    }
}
