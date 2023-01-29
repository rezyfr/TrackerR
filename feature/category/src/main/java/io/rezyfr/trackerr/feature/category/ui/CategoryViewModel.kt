package io.rezyfr.trackerr.feature.category.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.usecase.AddCategoryUseCase
import io.rezyfr.trackerr.core.domain.usecase.GetIconsUseCase
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import io.rezyfr.trackerr.feature.category.model.CategoryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getIconsUseCase: GetIconsUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) : SimpleFlowViewModel<CategoryDialogState, CategoryDialogEvent>() {

    override val initialUi: CategoryDialogState = CategoryDialogState(
        icons = listOf(),
        enabledButton = false,
        saveCategoryResult = ResultState.Uninitialized,
        cat = CategoryUiModel.emptyData(),
    )

    @TransactionType
    private var cat = MutableStateFlow(initialUi.cat)
    private var catAddResult = MutableStateFlow(initialUi.saveCategoryResult)
    private var icons = MutableStateFlow(initialUi.icons)

    override val uiFlow: Flow<CategoryDialogState> = combine(
        cat, catAddResult, icons
    ) { cat, result, iconList ->
        CategoryDialogState(
            enabledButton = cat.name.isNotEmpty() && cat.url.isNotEmpty() && cat.color.toArgb() != 0,
            cat = cat,
            icons = iconList,
            saveCategoryResult = result,
        )
    }

    override suspend fun handleEvent(event: CategoryDialogEvent) {
        when (event) {
            is CategoryDialogEvent.Initial -> {
                getIconsUseCase.invoke(Unit).collect {
                    icons.value = it
                }
            }
            is CategoryDialogEvent.OnChangeName -> {
                cat.value = cat.value.copy(name = event.name)
            }
            is CategoryDialogEvent.OnChooseIcon -> {
                cat.value = cat.value.copy(url = event.url)
            }
            is CategoryDialogEvent.OnChooseColor -> {
                cat.value = cat.value.copy(color = event.color)
            }
            is CategoryDialogEvent.OnChooseType -> {
                cat.value = cat.value.copy(type = event.type)
            }
            CategoryDialogEvent.OnSaveCategory
            -> {
                saveCategory()
            }
        }
    }

    private fun saveCategory() {
        viewModelScope.launch {
            val category = cat.value
            val result = addCategoryUseCase(
                CategoryModel(
                    name = category.name,
                    icon = category.url,
                    color = category.color.toArgb(),
                    id = "",
                    type = category.type,
                    userId = ""
                )
            )
            catAddResult.value = result
        }
    }

    init {
        viewModelScope.launch {
            handleEvent(CategoryDialogEvent.Initial)
        }
    }
}

data class CategoryDialogState(
    val saveCategoryResult: ResultState<Nothing?>,
    val icons: List<String>,
    val enabledButton: Boolean,
    val cat: CategoryUiModel
)

sealed interface CategoryDialogEvent {
    object Initial : CategoryDialogEvent
    data class OnChooseIcon(val url: String) : CategoryDialogEvent
    data class OnChangeName(val name: String) : CategoryDialogEvent
    data class OnChooseColor(val color: Color) : CategoryDialogEvent
    data class OnChooseType(@TransactionType val type: String) : CategoryDialogEvent
    object OnSaveCategory : CategoryDialogEvent
}
