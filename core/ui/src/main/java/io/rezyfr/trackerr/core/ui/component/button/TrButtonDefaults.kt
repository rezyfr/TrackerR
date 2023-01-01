package io.rezyfr.trackerr.core.ui.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.PurpleGray80

object TrButtonDefaults {
    fun padding(
        vertical: Dp = 12.dp,
        horizontal: Dp = 24.dp
    ): PaddingValues = PaddingValues(vertical = vertical, horizontal = horizontal)

    fun paddingWithStartIcon(
        start: Dp = 16.dp,
        top: Dp = 12.dp,
        end: Dp = 12.dp,
        bottom: Dp = 24.dp
    ): PaddingValues = PaddingValues(bottom = bottom, top = top, start = start, end = end)

    @Composable
    fun dangerColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    )

    @Composable
    fun primaryColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = PurpleGray80,
    )
}