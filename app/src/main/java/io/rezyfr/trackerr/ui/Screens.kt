package io.rezyfr.trackerr.ui

sealed class Screens(val route: String) {
    object DashboardScreen : Screens("dashboard_screen")
    object LoginScreen : Screens("login_screen")

    object ProfileScreen : Screens("profile_screen")
}

sealed class Dialog(val route: String){
    object TransactionDialog : Dialog("transaction_dialog/{trxId}") {
        const val ARG_TRANSACTION = "trxId"
    }
}
