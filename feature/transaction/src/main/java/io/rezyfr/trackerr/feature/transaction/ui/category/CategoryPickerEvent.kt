package io.rezyfr.trackerr.feature.transaction.ui.category

import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.model.CategoryModel

sealed interface CategoryPickerEvent {
    data class Initial(@TransactionType val type: String) : CategoryPickerEvent
    data class CategorySelected(val wallet: CategoryModel?) : CategoryPickerEvent
}