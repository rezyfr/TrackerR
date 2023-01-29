package io.rezyfr.trackerr.core.ui.component.picker.type

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.ui.component.MultiSelector
import io.rezyfr.trackerr.core.ui.typeIndicatorColor

@Composable
fun TypeSelector(
    modifier: Modifier = Modifier,
    @TransactionType type: String,
    onSelectType: (String) -> Unit = {},
    useDefaultColor: Boolean = true,
    backgroundColor: Color? = null,
    selectedHighlightColor: Color? = null,
) {
    MultiSelector (
        options = listOf(TransactionType.EXPENSE, TransactionType.INCOME),
        selectedOption = type,
        onOptionSelect = onSelectType,
        backgroundColor = if(useDefaultColor) type.typeIndicatorColor() else backgroundColor!!,
        selectedHighlightColor = if(useDefaultColor) MaterialTheme.colorScheme.background else selectedHighlightColor!!,
        selectedColor = if(useDefaultColor) type.typeIndicatorColor() else backgroundColor!!,
        unselectedColor = if(useDefaultColor) MaterialTheme.colorScheme.background else selectedHighlightColor!!,
        modifier = modifier
            .height(36.dp)
            .padding(horizontal = 36.dp)
    )
}