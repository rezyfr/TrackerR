package io.rezyfr.trackerr.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import io.rezyfr.trackerr.core.ui.component.AnimatedModalBottomSheetTransition
import io.rezyfr.trackerr.core.ui.component.TrBackground
import io.rezyfr.trackerr.core.ui.icon.Icon
import io.rezyfr.trackerr.navigation.NavGraphs
import io.rezyfr.trackerr.navigation.currentNavigator
import io.rezyfr.trackerr.navigation.navGraph
import io.rezyfr.trackerr.navigation.print
import io.rezyfr.trackerr.ui.navigation.BottomNavDestination
import io.rezyfr.trackerr.ui.navigation.TrBottomBar
import io.rezyfr.trackerr.ui.navigation.TrNavigationBar
import io.rezyfr.trackerr.ui.navigation.TrNavigationBarItem


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TrApp(appState: TrAppState = rememberTrAppState()) {
    TrBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            modifier = Modifier,
            bottomBar = {
                val currentSelectedItem by appState.navController.currentScreenAsState()
                AnimatedModalBottomSheetTransition(visible = appState.shouldShowBottomBar) {
                    TrBottomBar(
                        destinations = appState.bottomNavDestinations,
                        selectedNavigation = currentSelectedItem,
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
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {
            BackHandler {
                if (!appState.navController.popBackStack()) {
                    appState.navController.popBackStack()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                DestinationsNavHost(
                    navController = appState.navController,
                    dependenciesContainerBuilder = {
                        dependency(currentNavigator())
                    },
                    navGraph = NavGraphs.root
                )
                if (appState.shouldShowBottomBar) {
                    FloatingActionButton(
                        onClick = {
                            appState.navController.navigate(NavGraphs.transaction)
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
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
