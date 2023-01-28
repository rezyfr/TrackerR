package io.rezyfr.trackerr.feature.category.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.ChooseIconButton
import io.rezyfr.trackerr.core.ui.component.HSpacer
import io.rezyfr.trackerr.core.ui.component.ModalTransitionDialog
import io.rezyfr.trackerr.core.ui.component.TrTextField
import io.rezyfr.trackerr.core.ui.component.button.TrPrimaryButton
import io.rezyfr.trackerr.feature.category.component.CategoryAppBar


interface CategoryDialogNavigator {
    fun navigateUp()
}


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@Destination()
fun CategoryDialog(
    modifier: Modifier = Modifier,
    navigator: CategoryDialogNavigator,
    viewModel: CategoryViewModel = hiltViewModel(),
    catId: String? = null
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.onEvent(CategoryDialogEvent.Initial)
    ModalTransitionDialog(
        onDismissRequest = {
            navigator.navigateUp()
        },
    ) { transitionDialogHelper ->
//        if (state.saveTransactionResult is ResultState.Success) {
//            transitionDialogHelper::triggerAnimatedClose.invoke()
//        }
        CategoryDialog(
            modifier = modifier,
            state = state,
            onCloseClick = { transitionDialogHelper::triggerAnimatedClose.invoke() },
//            onSelectType = { (viewModel::onEvent)(TransactionEvent.OnSelectType(it)) },
//            onSelectWallet = { (viewModel::onEvent)(TransactionEvent.OnSelectWallet(it)) },
//            onChangeAmount = { (viewModel::onEvent)(TransactionEvent.OnChangeAmount(it)) },
//            onChangeDescription = { (viewModel::onEvent)(TransactionEvent.OnChangeDescription(it)) },
//            onSelectCategory = { (viewModel::onEvent)(TransactionEvent.OnSelectCategory(it)) },
//            onPickDate = { (viewModel::onEvent)(TransactionEvent.OnSelectDate(it)) },
//            onSaveTransaction = { (viewModel::onEvent)(TransactionEvent.OnSaveTransaction) },
//            onDeleteTransaction = { (viewModel::onEvent)(TransactionEvent.OnDeleteTransaction) },
//            onDeleteClick = { (viewModel::onEvent)(TransactionEvent.OnClickDeleteButton) }
        )
    }
}

@Composable
fun CategoryDialog(
    modifier: Modifier = Modifier,
    state: CategoryDialogState,
    onCloseClick: () -> Unit = {},
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
            CategoryTextField(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                chosenIcon = state.chosenIcon,
                icons = state.icons,
            )
            SaveCategoryButton()
        }
    }
}

@Composable
fun CategoryTextField(
    modifier: Modifier = Modifier,
    chosenIcon: String = "",
    icons: List<String> = listOf(),
    onIconChoose: (String) -> Unit = {},
    onClick: () -> Unit = {},
) {
    var value by remember { mutableStateOf("") }
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ChooseIconButton (
            chosenIcon,
            icons,
            onIconChoose = onIconChoose
        ) {
          onClick()
        }
        HSpacer()
        TrTextField(
            placeholder = "Write a category...",
            value = value,
            onValueChange = {
                value = it
            }
        )
    }
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