package io.rezyfr.trackerr.navigation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import io.rezyfr.tracker.feature.profile.ui.destinations.ProfileScreenDestination
import io.rezyfr.trackerr.feature.auth.destinations.AuthScreenDestination
import io.rezyfr.trackerr.feature.dashboard.destinations.DashboardScreenDestination
import io.rezyfr.trackerr.feature.transaction.destinations.TransactionDialogDestination

@ExperimentalComposeUiApi
object NavGraphs {

    val transaction = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>>  = listOf<DestinationSpec<*>>(
                TransactionDialogDestination
            ).associateBy { it.route }

        override val route: String = "transaction"
        override val startRoute: Route = TransactionDialogDestination

    }

    val dashboard = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>> = listOf<DestinationSpec<*>>(
            DashboardScreenDestination
        ).associateBy { it.route }

        override val route: String = "dashboard"
        override val startRoute: Route = DashboardScreenDestination

    }

    val auth = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>>  = listOf<DestinationSpec<*>>(
                AuthScreenDestination
            ).associateBy { it.route }

        override val route: String = "auth"
        override val startRoute: Route = AuthScreenDestination
    }

    val profile = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>> = listOf<DestinationSpec<*>>(
            ProfileScreenDestination
        ).associateBy { it.route }

        override val route: String = "profile"
        override val startRoute: Route = ProfileScreenDestination

    }

    val root = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>> = emptyMap()
        override val route: String = "root"
        override val startRoute: Route = auth
        override val nestedNavGraphs: List<NavGraphSpec> = listOf(
                auth,
                dashboard,
                transaction,
                profile
            )
    }
}

fun DestinationScope<*>.currentNavigator(): CommonNavGraphNavigator {
    return CommonNavGraphNavigator(
        navBackStackEntry.destination.navGraph(),
        navController,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }
    throw RuntimeException("Unknown nav graph for destination $route")
}

fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = toMutableList()
        .map { it.destination.route }
        .toTypedArray().contentToString()
    println("$prefix = $stack")
}
