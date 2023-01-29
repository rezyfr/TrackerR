package io.rezyfr.trackerr.core.ui.component.picker.datetime
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.component.picker.datetime.SelectorProperties
import io.rezyfr.trackerr.core.ui.component.picker.datetime.WheelPicker
import io.rezyfr.trackerr.core.ui.component.picker.datetime.WheelPickerDefaults

@Composable
internal fun WheelTextPicker(
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    size: DpSize = DpSize(128.dp, 128.dp),
    texts: List<String>,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onScrollFinished: (snappedIndex: Int) -> Int? = { null },
) {
    WheelPicker(
        modifier = modifier,
        startIndex = startIndex,
        size = size,
        count = texts.size,
        selectorProperties = selectorProperties,
        onScrollFinished = onScrollFinished
    ){ index ->
        Text(
            text = texts[index],
            style = style,
            color = color,
            maxLines = 1
        )
    }
}