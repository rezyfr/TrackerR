package io.rezyfr.trackerr.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.PurpleGray60
import io.rezyfr.trackerr.core.ui.PurpleGray80

/**
 * TrackerR filled button with generic content slot. Wraps Material 3 [Button].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */

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
}
@Composable
fun TrButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = PurpleGray80,
    ),
    contentPadding: PaddingValues = TrButtonDefaults.padding(),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
        shape = RoundedCornerShape(8.dp)
    )
}

/**
 * TrackerR filled button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 */
@Composable
fun TrButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    TrButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) {
            TrButtonDefaults.paddingWithStartIcon()
        } else {
            TrButtonDefaults.padding()
        }
    ) {
        TrButtonContent(
            text = text,
            leadingIcon = leadingIcon
        )
    }
}

/**
 * Internal TrackerR button content layout for arranging the text label and leading icon.
 *
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Default is `null` for no leading icon.Ï
 */
@Composable
private fun TrButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier
            .padding(
                start = if (leadingIcon != null) {
                    ButtonDefaults.IconSpacing
                } else {
                    0.dp
                }
            )
    ) {
        text()
    }
}
