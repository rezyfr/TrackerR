package io.rezyfr.trackerr.feature.category.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.ui.component.ModalTransitionDialog
import io.rezyfr.trackerr.feature.category.component.CategoryAppBar


interface CategoryDialogNavigator {
    fun navigateUp()
}


@Composable
@Destination()
fun CategoryDialog(
    modifier: Modifier = Modifier,
    navigator: CategoryDialogNavigator,
    catId: String? = null
) {
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
//            state = state,
//            onCloseClick = { transitionDialogHelper::triggerAnimatedClose.invoke() },
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
    modifier: Modifier = Modifier
) {
    Box (
        modifier.background(color = MaterialTheme.colorScheme.primary)
    ) {
        CategoryAppBar(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter))
    }
}