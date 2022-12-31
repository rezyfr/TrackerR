package io.rezyfr.trackerr.feature.transaction.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.ui.component.MultiSelector
import io.rezyfr.trackerr.core.ui.typeIndicatorColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAppBar(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    @TransactionType type: String,
    onSelectType: (String) -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            MultiSelector(
                options = listOf(TransactionType.EXPENSE, TransactionType.INCOME),
                selectedOption = type,
                onOptionSelect = onSelectType,
                backgroundColor = type.typeIndicatorColor(),
                selectedHighlightColor = MaterialTheme.colorScheme.background,
                selectedColor = type.typeIndicatorColor(),
                unselectedColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .height(36.dp)
                    .padding(horizontal = 36.dp)
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(
                onClick = onCloseClick
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        modifier = modifier
    )
}