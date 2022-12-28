package io.rezyfr.trackerr.feature.homescreen.ui.category

import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.SelectableWalletModel
import io.rezyfr.trackerr.core.domain.model.WalletModel

sealed interface CategoryPickerState {
    object Uninitialized : CategoryPickerState
    data class Success(val categories: List<CategoryModel>, val selectedCategory: CategoryModel?) :
        CategoryPickerState

    data class Error(val error: Throwable) : CategoryPickerState
}
