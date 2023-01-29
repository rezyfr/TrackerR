package io.rezyfr.trackerr.feature.category.model

import androidx.compose.ui.graphics.Color
import io.rezyfr.trackerr.common.TransactionType

data class CategoryUiModel(
    val url: String,
    val name: String,
    @TransactionType val type: String,
    val color: Color
) {
    companion object {
        fun emptyData() = CategoryUiModel(
            url = "",
            name = "",
            type = TransactionType.EXPENSE,
            color = Color(0xffFFFFFF)
        )
    }
}
