package io.rezyfr.trackerr.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.ExperimentalComposeUiApi
import com.ramcosta.composedestinations.spec.NavGraphSpec
import io.rezyfr.trackerr.core.ui.icon.Icon
import io.rezyfr.trackerr.navigation.NavGraphs

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
@OptIn(ExperimentalComposeUiApi::class)
enum class BottomNavDestination(
    val screen: NavGraphSpec,
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: String,
    val titleTextId: String
) {
    DASHBOARD(
        screen = NavGraphs.dashboard,
        selectedIcon = Icon.ImageVectorIcon(Icons.Default.Home),
        unselectedIcon = Icon.ImageVectorIcon(Icons.Default.Home),
        iconTextId = "Dashboard",
        titleTextId = "Dashboard"
    ),

    PROFILE(
        screen = NavGraphs.profile,
        selectedIcon = Icon.ImageVectorIcon(Icons.Default.AccountCircle),
        unselectedIcon = Icon.ImageVectorIcon(Icons.Default.AccountCircle),
        iconTextId = "Profile",
        titleTextId = "Profile"
    )
}
