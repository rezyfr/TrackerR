package io.rezyfr.trackerr.feature.transaction.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.ui.component.CloseButton
import io.rezyfr.trackerr.core.ui.component.picker.type.TypeSelector

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
            TypeSelector(
                type = type,
                onSelectType = onSelectType,
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