package io.rezyfr.trackerr.feature.homescreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.icon.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTextField(
    value: String? = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit = {},
    trailingIcon: Icon.ImageVectorIcon? = null,
    onClick: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value.orEmpty(),
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.outline.copy(
                        alpha = 0.5f
                    )
                ),
                textAlign = TextAlign.Center
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions(onDone = { }),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        trailingIcon = {
            if (trailingIcon != null) {
                Icon(
                    trailingIcon.imageVector,
                    contentDescription = "trailing_icon"
                )
            }
        },
        enabled = onClick == null
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000a12)
@Composable
fun PreviewTrxTextField() {
    TrTheme {
        Box(Modifier.padding(16.dp)) {
            TransactionTextField(
                placeholder = "Category"
            )
        }
    }
}