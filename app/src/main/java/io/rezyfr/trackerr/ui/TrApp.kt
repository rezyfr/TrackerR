package io.rezyfr.trackerr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import io.rezyfr.trackerr.core.ui.component.AnimatedModalBottomSheetTransition
import io.rezyfr.trackerr.core.ui.component.TrBackground
import io.rezyfr.trackerr.navigation.NavGraphs
import io.rezyfr.trackerr.navigation.currentNavigator
import io.rezyfr.trackerr.navigation.navGraph
import io.rezyfr.trackerr.navigation.print
import io.rezyfr.trackerr.ui.navigation.TrBottomBar


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TrApp(appState: TrAppState = rememberTrAppState()) {
    TrBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            modifier = Modifier,
            bottomBar = {
                AnimatedModalBottomSheetTransition(visible = appState.shouldShowBottomBar) {
                    TrBottomBar(
                        destinations = appState.bottomNavDestinations,
                        selectedNavigation = appState.currentDestination,
                        onNavigationSelected = { selected ->
                            appState.navController.navigate(selected) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(appState.navController.graph.findStartDestination().id)
                            }
                        },
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                DestinationsNavHost(
                    navController = appState.navController,
                    dependenciesContainerBuilder = {
                        dependency(currentNavigator())
                    },
                    navGraph = NavGraphs.root
                )
            }
        }
    }
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
