package io.rezyfr.trackerr.core.ui.component.picker.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.TrTheme

@Composable
fun ColorPicker(modifier: Modifier = Modifier, color: Color, selected: Boolean, onSelected: (Color) -> Unit = {}) {
    Box(
        modifier = modifier
            .size(30.dp)
            .background(color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                onSelected(color)
            }
    ) {
        Box(
            Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color = color)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffFCFCFC)
@Composable
fun ColorPickerPreview() {
    TrTheme() {
        ColorPicker(color = Color.Red, selected = false)
    }
}