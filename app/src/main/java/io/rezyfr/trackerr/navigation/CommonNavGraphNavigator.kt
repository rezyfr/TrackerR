package io.rezyfr.trackerr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import io.rezyfr.tracker.feature.profile.ui.ProfileNavigator
import io.rezyfr.trackerr.feature.auth.AuthNavigator
import io.rezyfr.trackerr.feature.auth.destinations.AuthScreenDestination
import io.rezyfr.trackerr.feature.dashboard.DashboardNavigator
import io.rezyfr.trackerr.feature.dashboard.destinations.DashboardScreenDestination
import io.rezyfr.trackerr.feature.transaction.TransactionDialogNavigator
import io.rezyfr.trackerr.feature.transaction.destinations.TransactionDialogDestination

class CommonNavGraphNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController
) : AuthNavigator, DashboardNavigator, TransactionDialogNavigator, ProfileNavigator {
    override fun onLoginSuccess() {
        navController.navigate(DashboardScreenDestination) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
            }
            launchSingleTop = true
        }
    }

    override fun openTransactionWithId(trxId: String) {
        navController.navigate(TransactionDialogDestination(trxId))
    }

    override fun openNewTransactionDialog() {
        navController.navigate(TransactionDialogDestination())
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun onLogoutSuccess() {
        navController.navigate(AuthScreenDestination()) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
                inclusive = true
            }
        }
    }
}