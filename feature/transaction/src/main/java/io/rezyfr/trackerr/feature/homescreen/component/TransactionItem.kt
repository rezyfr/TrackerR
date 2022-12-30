package io.rezyfr.trackerr.feature.homescreen.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.model.previewTransactionModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.transactionIndicatorColor
import io.rezyfr.trackerr.feature.homescreen.model.TransactionUiModel
import io.rezyfr.trackerr.feature.homescreen.model.asUiModel

@Composable
fun TransactionItem(
    transaction: TransactionUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TransactionIcon(
            color = transaction.amount.transactionIndicatorColor(transaction.isIncome),
            Modifier
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = transaction.wallet.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            )
        }
        Text(
            text = NumberUtils.getNominalFormat(transaction.amount),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TransactionIcon(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(height = 32.dp, width = 3.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DarkDefaultPreview() {
    TrTheme(darkTheme = true) {
        TransactionItem(
            transaction = previewTransactionModel.last().asUiModel(),
        )
    }
}