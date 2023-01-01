package io.rezyfr.trackerr.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TrAlertDialog(
    title: String,
    message: String,
    onDismiss: (() -> Unit)? = null,
    onConfirm: () -> Unit,
    confirmText: String,
    confirmColor: Color = MaterialTheme.colorScheme.onBackground,
    dismissText: String,
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss?.invoke()
        },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    confirmText,
                    style = MaterialTheme.typography.titleSmall,
                    color = confirmColor
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss?.invoke()
            }) {
                Text(
                    dismissText,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
    )
}