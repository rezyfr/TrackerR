package io.rezyfr.trackerr.ui

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.ramcosta.composedestinations.spec.NavGraphSpec
import io.rezyfr.trackerr.navigation.NavGraphs
import io.rezyfr.trackerr.navigation.navGraph
import io.rezyfr.trackerr.navigation.print
import io.rezyfr.trackerr.ui.navigation.BottomNavDestination
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
    val currentDestination: NavGraphSpec
        @Composable get() = navController
            .currentScreenAsState().value

    @OptIn(ExperimentalComposeUiApi::class)
    private val currentBottomNavDestination: NavGraphSpec?
        @Composable get() = when (currentDestination.route) {
            NavGraphs.dashboard.route -> NavGraphs.dashboard
            NavGraphs.profile.route -> NavGraphs.profile
            else -> null
        }

    var shouldShowSettingsDialog by mutableStateOf(false)
        private set

    val shouldShowBottomBar: Boolean
        @Composable get() = currentBottomNavDestination != null
    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val bottomNavDestinations: List<BottomNavDestination> = BottomNavDestination.values().asList()

    fun onBackClick() {
        navController.popBackStack()
    }

    fun setShowSettingsDialog(shouldShow: Boolean) {
        shouldShowSettingsDialog = shouldShow
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Stable
    @Composable
    private fun NavController.currentScreenAsState(): State<NavGraphSpec> {
        val selectedItem = remember { mutableStateOf(NavGraphs.dashboard) }

        DisposableEffect(this) {
            val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                backQueue.print()
                selectedItem.value = destination.navGraph()
            }
            addOnDestinationChangedListener(listener)

            onDispose {
                removeOnDestinationChangedListener(listener)
            }
        }

        return selectedItem
    }
}
