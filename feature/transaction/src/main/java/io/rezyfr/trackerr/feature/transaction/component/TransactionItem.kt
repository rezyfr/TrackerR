package io.rezyfr.trackerr.feature.transaction.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.rezyfr.trackerr.core.domain.mapper.NumberUtils
import io.rezyfr.trackerr.core.domain.model.previewTransactionModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.feature.transaction.model.TransactionUiModel
import io.rezyfr.trackerr.feature.transaction.model.asUiModel

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
            transaction = transaction,
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
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = NumberUtils.getRupiahCurrency(transaction.amount),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = transaction.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun TransactionIcon(transaction: TransactionUiModel, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(model = transaction.category.icon),
        contentDescription = null,
        modifier = Modifier
            .size(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = Color(transaction.category.color).copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        colorFilter = ColorFilter.tint(Color(transaction.category.color))
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DarkDefaultPreview() {
    TrTheme(darkTheme = true) {
        TransactionItem(
            transaction = previewTransactionModel.last().asUiModel(true),
        )
    }
}