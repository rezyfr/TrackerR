package io.rezyfr.trackerr.feature.homescreen.ui.category

import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.usecase.GetCategoriesUseCase
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class CategoryPickerViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : SimpleFlowViewModel<CategoryPickerState, CategoryPickerEvent>() {

    private val selectedWallet = MutableStateFlow<CategoryModel?>(null)
    private val trxType = MutableStateFlow(TransactionType.EXPENSE)
    override val initialUi: CategoryPickerState = CategoryPickerState.Uninitialized
    override val uiFlow: Flow<CategoryPickerState>
        get() = combine(categoriesWithTypeFlow(), selectedWallet) { categories, selectedCategory ->
            CategoryPickerState.Success(categories, selectedCategory)
        }.catch {
            CategoryPickerState.Error(it)
        }

    private fun categoriesWithTypeFlow() = combine(getCategoriesUseCase(), trxType) { categories, type ->
        categories.filter { it.type == type }
    }

    override suspend fun handleEvent(event: CategoryPickerEvent) {
        when (event) {
            is CategoryPickerEvent.CategorySelected -> {
                handleSelectedWallet(event.wallet)
            }
            is CategoryPickerEvent.Initial -> {
                handleInitial(event.type)
            }
        }
    }

    private fun handleInitial(@TransactionType type: String) {
        trxType.value = type
    }

    private fun handleSelectedWallet(wallet: CategoryModel?) {
        selectedWallet.value = wallet
    }
}