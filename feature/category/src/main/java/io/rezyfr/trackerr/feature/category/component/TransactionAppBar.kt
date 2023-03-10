package io.rezyfr.trackerr.feature.category.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.component.CloseButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAppBar(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            CloseButton {
                onCloseClick()
            }
        },
        modifier = modifier
    )
}