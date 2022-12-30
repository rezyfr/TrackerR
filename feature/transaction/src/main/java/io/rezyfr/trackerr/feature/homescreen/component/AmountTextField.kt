package io.rezyfr.trackerr.feature.homescreen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.core.ui.component.BottomSheet
import io.rezyfr.trackerr.core.ui.component.PrefixTransformation
import io.rezyfr.trackerr.core.ui.component.TransparentText
import io.rezyfr.trackerr.feature.homescreen.TransactionEvent
import io.rezyfr.trackerr.feature.homescreen.TransactionState
import io.rezyfr.trackerr.feature.homescreen.model.TransactionUiModel

@Composable
fun AmountTextField(
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    value: TextFieldValue
) {
    Column(modifier.padding(16.dp)) {
        Text(
            "how much?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.65f)
        )
        BasicTextField(
            visualTransformation = PrefixTransformation("Rp"),
            value = value,
            decorationBox = { innerTextField ->
                if (value.text.isEmpty()) {
                    Row {
                        TransparentText("Rp", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.W500),)
                        Text(
                            "0",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.W500
                            ),
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.65f),
                        )
                    }
                }
                innerTextField()
            },
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.background
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.background),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Preview() {
    MaterialTheme {
        AmountTextField(
            onValueChange = {},
            value = TextFieldValue("100000")
        )
    }
}