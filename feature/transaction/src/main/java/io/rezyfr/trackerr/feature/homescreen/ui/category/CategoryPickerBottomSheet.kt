package io.rezyfr.trackerr.feature.homescreen.ui.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.BottomSheet
import io.rezyfr.trackerr.core.ui.component.TrxBottomSheet
import io.rezyfr.trackerr.core.ui.util.hiltViewModelPreviewSafe


@Composable
fun BoxScope.CategoryPickerBottomSheet(
    bottomSheet: BottomSheet,
    selected: CategoryModel? = null,
    @TransactionType type: String,
    onSelect: (CategoryModel?) -> Unit = {}
) {
    val viewModel: CategoryPickerViewModel? = hiltViewModelPreviewSafe()
    val categoryPickerState = viewModel?.uiState?.collectAsState()?.value ?: previewState()

    LaunchedEffect(key1 = selected) {
        viewModel?.onEvent(CategoryPickerEvent.CategorySelected(selected))
    }

    LaunchedEffect(key1 = type) {
        viewModel?.onEvent(CategoryPickerEvent.Initial(type))
    }

    TrxBottomSheet(bottomSheet = bottomSheet) {
        LazyColumn() {
            item(key = "title") {
                Text(
                    "Choose Category",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )
            }
            if (categoryPickerState is CategoryPickerState.Success) {
                items(categoryPickerState.categories, key = { it.id }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(it)
                                bottomSheet.collapse()
                            }
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Divider(modifier = Modifier.align(Alignment.BottomCenter))
                    }

                }
            }
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    TrTheme {
        Box {
            CategoryPickerBottomSheet(
                bottomSheet = BottomSheet().apply {
                    expand()
                },
                selected = CategoryModel(
                    id = "1",
                    name = "Category 1",
                    userId = "1",
                    type = "expense"
                ),
                type = "expense"
            ) {}
        }
    }
}

private fun previewState() = CategoryPickerState.Success(
    categories = listOf(
        CategoryModel(
            id = "1", userId = "0",
            name = "Category 1", type = "expense"
        ),
        CategoryModel(
            id = "2", userId = "0",
            name = "Category 2", type = "expense"
        ),
        CategoryModel(
            id = "3", userId = "0",
            name = "Category 3", type = "expense"
        ),
        CategoryModel(
            type = "expense",
            id = "4", userId = "0",
            name = "Category 4"
        ),
        CategoryModel(
            id = "5", userId = "0",
            name = "Category 1",
            type = "income",
        )
    ),
    selectedCategory = CategoryModel(
        id = "1", userId = "0",
        name = "Category 1",
        type = "income",
    )
)
