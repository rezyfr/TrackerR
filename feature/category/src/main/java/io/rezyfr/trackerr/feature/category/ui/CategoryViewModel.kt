package io.rezyfr.trackerr.feature.category.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.usecase.GetIconsUseCase
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import io.rezyfr.trackerr.feature.category.model.CategoryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getIconsUseCase: GetIconsUseCase
) : SimpleFlowViewModel<CategoryDialogState, CategoryDialogEvent>() {

    override val initialUi: CategoryDialogState = CategoryDialogState(
        chosenIcon = "",
        icons = listOf(),
        enabledButton = false,
        saveCategoryResult = ResultState.Uninitialized,
        cat = CategoryUiModel.emptyData()
    )

    @TransactionType
    private var cat = MutableStateFlow(initialUi.cat)
    private var catAddResult = MutableStateFlow(initialUi.saveCategoryResult)
    private var icons = MutableStateFlow(initialUi.icons)

    override val uiFlow: Flow<CategoryDialogState> = combine(
        cat, catAddResult, icons
    ) { cat, result, iconList ->
        CategoryDialogState(
            enabledButton = cat.name.isNotEmpty() && cat.url.isNotEmpty(),
            cat = cat,
            chosenIcon = cat.url,
            icons = iconList,
            saveCategoryResult = result
        )
    }

    override suspend fun handleEvent(event: CategoryDialogEvent) {
        when (event) {
            is CategoryDialogEvent.Initial -> {
                getIconsUseCase.invoke(Unit).collect {
                    icons.value = it
                }
            }
            is CategoryDialogEvent.OnChangeCategoryName -> TODO()
            is CategoryDialogEvent.OnChooseIcon -> TODO()
            CategoryDialogEvent.OnSaveCategory -> TODO()
        }
    }


}

data class CategoryDialogState(
    val saveCategoryResult: ResultState<Nothing?>,
    val chosenIcon: String,
    val icons: List<String>,
    val enabledButton: Boolean,
    val cat: CategoryUiModel
)

sealed interface CategoryDialogEvent {
    object Initial : CategoryDialogEvent
    data class OnChooseIcon(val category: CategoryModel) : CategoryDialogEvent
    data class OnChangeCategoryName(val name: String) : CategoryDialogEvent
    object OnSaveCategory : CategoryDialogEvent
}
