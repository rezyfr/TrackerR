package io.rezyfr.trackerr.feature.transaction.ui.datepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.ui.component.BottomSheet
import io.rezyfr.trackerr.core.ui.component.BottomSheetTitle
import io.rezyfr.trackerr.core.ui.component.TrxBottomSheet
import io.rezyfr.trackerr.core.ui.component.button.TrPrimaryButton
import io.rezyfr.trackerr.core.ui.component.picker.datetime.WheelDatePicker
import io.rezyfr.trackerr.core.ui.component.picker.datetime.WheelPickerDefaults
import java.time.LocalDate

@Composable
fun BoxScope.DatePickerBottomSheet(
    bottomSheet: BottomSheet,
    startDate: LocalDate,
    onPick: (LocalDate) -> Unit,
) {
    var selectedDate by remember { mutableStateOf(startDate) }
    TrxBottomSheet(bottomSheet = bottomSheet) {
        LazyColumn(Modifier.fillMaxWidth()) {
            item(key = "title") {
                BottomSheetTitle("Pick a date")
            }
            item() {
                TransactionDatePicker(
                    onPick = {
                        selectedDate = it
                    },
                    startDate = startDate
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                TrPrimaryButton(
                    onClick = {
                        onPick(selectedDate)
                        bottomSheet.collapse()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Continue"
                )
            }
        }
    }
}

@Composable
private fun BoxScope.TransactionDatePicker(
    onPick: (LocalDate) -> Unit,
    startDate: LocalDate,
) {
    WheelDatePicker(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center),
        selectorProperties = WheelPickerDefaults.selectorProperties(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        ),
        startDate = startDate,
        onSnappedDate = onPick,
        yearsRange = IntRange(2020, startDate.year),
    )
}