package io.rezyfr.trackerr.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import io.rezyfr.trackerr.core.ui.icon.Icon

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: String,
    val titleTextId: String
) {
    TRANSACTIONS(
        selectedIcon = Icon.ImageVectorIcon(Icons.Default.KeyboardArrowRight),
        unselectedIcon = Icon.ImageVectorIcon(Icons.Default.KeyboardArrowRight),
        iconTextId = "Transactions",
        titleTextId = "Transactions"
    ),

    PROFILE(
        selectedIcon = Icon.ImageVectorIcon(Icons.Default.AccountCircle),
        unselectedIcon = Icon.ImageVectorIcon(Icons.Default.AccountCircle),
        iconTextId = "Profile",
        titleTextId = "Profile"
    )
}
