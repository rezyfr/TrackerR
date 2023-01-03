package io.rezyfr.trackerr.feature.dashboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rezyfr.trackerr.core.ui.Subdued
import io.rezyfr.trackerr.core.ui.TrTheme

@Composable
internal fun AccountBalance(
    balance: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Account balance",
            style = MaterialTheme.typography.labelMedium,
            color = Subdued,
        )
        Text(
            text = balance,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = (-1).sp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AccountBalancePreview(){
    TrTheme() {
        AccountBalance(balance = "Rp1.000.000")
    }
}
