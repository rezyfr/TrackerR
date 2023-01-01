package io.rezyfr.trackerr.core.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.VSpacer(height: Dp = 8.dp) {
    Spacer(Modifier.height(height))
}

@Composable
fun RowScope.HSpacer(width: Dp = 8.dp) {
    Spacer(Modifier
        .height(width)
    )
}

@Composable
fun ColumnScope.FullSpacer() {
    Spacer(Modifier.weight(1f))
}

@Composable
fun RowScope.FullSpacer() {
    Spacer(Modifier.weight(1f))
}