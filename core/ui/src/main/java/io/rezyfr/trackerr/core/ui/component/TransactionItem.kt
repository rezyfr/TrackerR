package io.rezyfr.trackerr.core.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.previewTransactionModel
import io.rezyfr.trackerr.core.ui.TrTheme

@Composable
fun TransactionItem(
    transaction: TransactionModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TransactionIcon(isIncome = transaction.isIncome, Modifier)
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Text(text = transaction.description, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onBackground)
            Text(text = transaction.wallet, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Text(text = transaction.amount, style = MaterialTheme.typography.titleSmall, color = if (!transaction.isIncome) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun TransactionIcon(isIncome: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(CircleShape)
            .size(32.dp)
            .background(if (!isIncome) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (!isIncome) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
            contentDescription = "Type Icon",
            tint = if (!isIncome) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    TrTheme() {
        TransactionItem(transaction = previewTransactionModel.first())
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DarkDefaultPreview() {
    TrTheme(darkTheme = true) {
        TransactionItem(transaction = previewTransactionModel.last())
    }
}