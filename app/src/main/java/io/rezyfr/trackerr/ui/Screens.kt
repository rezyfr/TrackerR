package io.rezyfr.trackerr.ui

sealed class Screens(val route: String) {
    object TransactionScreen : Screens("transaction_screen")
    object LoginScreen : Screens("login_screen")

    object ProfileScreen : Screens("profile_screen")
}
