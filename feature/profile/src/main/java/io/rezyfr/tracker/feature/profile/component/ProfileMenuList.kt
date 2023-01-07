package io.rezyfr.tracker.feature.profile.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector

fun getMenus(onCategoryClick: () -> Unit) = listOf(
    Item.Menu("Category", icon = Icons.Default.Category) {
        onCategoryClick.invoke()
    },
    Item.Menu( "Wallet", icon = Icons.Default.Wallet) {

    }
)

sealed class Item {
    class Menu(
        val text: String,
        val icon: ImageVector,
        val onClickListener: () -> Unit,
    ) : Item()
}