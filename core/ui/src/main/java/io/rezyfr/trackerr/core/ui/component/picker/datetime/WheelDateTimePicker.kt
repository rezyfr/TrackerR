package io.rezyfr.trackerr.core.ui.component.picker.datetime

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.component.picker.datetime.DefaultWheelDateTimePicker
import io.rezyfr.trackerr.core.ui.component.picker.datetime.SelectorProperties
import io.rezyfr.trackerr.core.ui.component.picker.datetime.TimeFormat
import io.rezyfr.trackerr.core.ui.component.picker.datetime.WheelPickerDefaults
import java.time.LocalDateTime

@Composable
fun WheelDateTimePicker(
    modifier: Modifier = Modifier,
    startDateTime: LocalDateTime = LocalDateTime.now(),
    yearsRange: IntRange? = IntRange(1922, 2122),
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    backwardsDisabled: Boolean = false,
    size: DpSize = DpSize(256.dp, 128.dp),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onSnappedDateTime : (snappedDateTime: LocalDateTime) -> Unit = {}
) {
    DefaultWheelDateTimePicker(
        modifier,
        startDateTime,
        yearsRange,
        timeFormat,
        backwardsDisabled,
        size,
        textStyle,
        textColor,
        selectorProperties,
        onSnappedDateTime = { snappedDateTime ->
            onSnappedDateTime(snappedDateTime.snappedLocalDateTime)
            snappedDateTime.snappedIndex
        }
    )
}