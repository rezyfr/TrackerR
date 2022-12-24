package io.rezyfr.trackerr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import io.rezyfr.trackerr.core.ui.component.TrBackground
import io.rezyfr.trackerr.core.ui.icon.Icon
import io.rezyfr.trackerr.ui.navigation.TopLevelDestination
import io.rezyfr.trackerr.ui.navigation.TrNavigationBar
import io.rezyfr.trackerr.ui.navigation.TrNavigationBarItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrApp(
    appState: TrAppState = rememberTrAppState()
) {
    TrBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            modifier = Modifier,
            bottomBar = {
                TrBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                TrNavigation(appState.navController)
            }
        }
    }
}

@Composable
private fun TrBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    TrNavigationBar(
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            TrNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )

                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(destination.iconTextId) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

