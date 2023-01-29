package io.rezyfr.trackerr.feature.category.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.ui.IconColors
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.ChooseIconButton
import io.rezyfr.trackerr.core.ui.component.HSpacer
import io.rezyfr.trackerr.core.ui.component.ModalTransitionDialog
import io.rezyfr.trackerr.core.ui.component.TrTextField
import io.rezyfr.trackerr.core.ui.component.button.TrPrimaryButton
import io.rezyfr.trackerr.core.ui.component.picker.color.ColorPicker
import io.rezyfr.trackerr.core.ui.component.picker.type.TypeSelector
import io.rezyfr.trackerr.feature.category.component.CategoryAppBar


interface CategoryDialogNavigator {
    fun navigateUp()
}


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@Destination
fun CategoryDialog(
    modifier: Modifier = Modifier,
    navigator: CategoryDialogNavigator,
    viewModel: CategoryViewModel = hiltViewModel(),
    catId: String? = null
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ModalTransitionDialog(
        onDismissRequest = {
            navigator.navigateUp()
        },
    ) { transitionDialogHelper ->
        if (state.saveCategoryResult is ResultState.Success) {
            transitionDialogHelper::triggerAnimatedClose.invoke()
        }
        CategoryDialog(
            modifier = modifier,
            state = state,
            onCloseClick = { transitionDialogHelper::triggerAnimatedClose.invoke() },
            onSelectIcon = { viewModel.onEvent(CategoryDialogEvent.OnChooseIcon(it)) },
            onSelectColor = { viewModel.onEvent(CategoryDialogEvent.OnChooseColor(it)) },
            onChangeName = { viewModel.onEvent(CategoryDialogEvent.OnChangeName(it)) },
            onSelectType = { viewModel.onEvent(CategoryDialogEvent.OnChooseType(it)) },
            onSaveCategory = { viewModel.onEvent(CategoryDialogEvent.OnSaveCategory) }
        )
    }
}

@Composable
fun CategoryDialog(
    modifier: Modifier = Modifier,
    state: CategoryDialogState,
    onCloseClick: () -> Unit = {},
    onSelectIcon: (String) -> Unit = {},
    onSelectColor: (Color) -> Unit = {},
    onChangeName: (String) -> Unit = {},
    onSelectType: (String) -> Unit = {},
    onSaveCategory: () -> Unit = {}
) {
    Box(
        modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxSize()
    ) {
        CategoryAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            onCloseClick = onCloseClick
        )
        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        topStart = 32.dp, topEnd = 32.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TypeSelector(
                type = state.cat.type,
                onSelectType = onSelectType,
                useDefaultColor = false,
                backgroundColor = MaterialTheme.colorScheme.background,
                selectedHighlightColor = MaterialTheme.colorScheme.primary
            )
            IconPickerField(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                chosenIcon = state.cat.url,
                chosenColor = state.cat.color,
                icons = state.icons,
                onChooseIcon = onSelectIcon,
                onChooseColor = { onSelectColor.invoke(it) }
            )
            CategoryTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                onChangeName = onChangeName,
                name = state.cat.name
            )
            SaveCategoryButton(
                onClick = onSaveCategory,
                enabledButton = state.enabledButton
            )
        }
    }
}

@Composable
fun IconPickerField(
    modifier: Modifier = Modifier,
    chosenIcon: String = "",
    chosenColor: Color = Color(0xFFFFFFFF),
    icons: List<String> = listOf(),
    onChooseIcon: (String) -> Unit = {},
    onChooseColor: (Color) -> Unit = {},
    onClick: () -> Unit = {},
) {
    Row(
        modifier
            .fillMaxWidth()
    ) {
        ChooseIconButton(
            chosenIcon,
            icons,
            color = chosenColor,
            onIconChoose = onChooseIcon
        ) {
            onClick()
        }
        HSpacer(16.dp)
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            items(IconColors) {
                ColorPicker(color = it, selected = chosenColor == it) { color ->
                    onChooseColor(color)
                }
            }
        }
    }
}

@Composable
fun CategoryTextField(
    modifier: Modifier = Modifier,
    onChangeName: (String) -> Unit = {},
    name: String = ""
) {
    TrTextField(
        placeholder = "Write a category...",
        value = name,
        onValueChange = onChangeName
    )
}

@Composable
fun SaveCategoryButton(
    onClick: () -> Unit = {},
    enabledButton: Boolean = false
) {
    TrPrimaryButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        text = "Save Category",
        enabled = enabledButton,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000a12)
@Composable
fun CategoryTextFieldDialogPreview() {
    TrTheme {
        CategoryTextField()
    }
}


@Preview(showBackground = true, backgroundColor = 0xffFCFCFC)
@Composable
fun IconPickerFieldPreview() {
    TrTheme {
        IconPickerField()
    }
}