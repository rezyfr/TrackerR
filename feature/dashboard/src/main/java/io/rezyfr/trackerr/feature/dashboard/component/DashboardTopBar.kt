package io.rezyfr.trackerr.feature.dashboard.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DashboardTopBar(
    modifier: Modifier = Modifier,
    profileUrl: String = "",
    selectedMonth: String = ""
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = profileUrl,
            contentDescription = "Profile image",
            modifier = Modifier
                .size(36.dp)
                .border(1.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .padding(3.dp)
                .clip(CircleShape),
        )
        Box(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .offset(x = (-20).dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 12.dp)
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Period picker",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    selectedMonth,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardTopBarPreview() {
    DashboardTopBar(selectedMonth = "September")
}