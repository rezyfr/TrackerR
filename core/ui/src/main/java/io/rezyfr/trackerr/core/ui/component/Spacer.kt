package io.rezyfr.trackerr.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VSpacer(height: Dp = 8.dp) {
    Spacer(Modifier.height(height))
}

@Composable
fun HSpacer(width: Dp = 8.dp) {
    Spacer(Modifier.width(width))
}

@Composable
fun RowScope.FullSpacer() {
    Spacer(Modifier.weight(1f))
}