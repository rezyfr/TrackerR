package io.rezyfr.trackerr.ui

import androidx.compose.runtime.*
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import io.rezyfr.trackerr.ui.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberTrAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): TrAppState {

    return remember(navController, coroutineScope) {
        TrAppState(navController, coroutineScope)
    }
}

@Stable
class TrAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            Screens.TransactionScreen.route -> TopLevelDestination.TRANSACTIONS
            Screens.ProfileScreen.route -> TopLevelDestination.PROFILE
            else -> null
        }

    var shouldShowSettingsDialog by mutableStateOf(false)
        private set

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.TRANSACTIONS -> navController.navigate(
                Screens.TransactionScreen.route,
                topLevelNavOptions
            )
            TopLevelDestination.PROFILE -> navController.navigate(
                Screens.ProfileScreen.route,
                topLevelNavOptions
            )
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }

    fun setShowSettingsDialog(shouldShow: Boolean) {
        shouldShowSettingsDialog = shouldShow
    }
}
