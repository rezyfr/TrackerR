package io.rezyfr.trackerr.core.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String? = null,
    size: Dp = 48.dp
) {
    AsyncImage(
        modifier = modifier
            .clip(CircleShape)
            .size(size),
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
    )
}