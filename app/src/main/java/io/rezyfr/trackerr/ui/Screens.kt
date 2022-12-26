package io.rezyfr.trackerr.ui

sealed class Screens(val route: String) {
    object DashboardScreen : Screens("dashboard_screen")
    object LoginScreen : Screens("login_screen")

    object ProfileScreen : Screens("profile_screen")
}
