/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rezyfr.trackerr.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.feature.dashboard.DashboardRoute
import io.rezyfr.trackerr.feature.homescreen.TransactionDialogRoute
import io.rezyfr.trackerr.feature.homescreen.model.TransactionUiModel
import io.rezyfr.trackerr.ui.auth.LoginRoute

@Composable
fun TrNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        composable(Screens.LoginScreen.route) {
            LoginRoute(
                onStoreUser = {
                    navController.navigate(Screens.DashboardScreen.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screens.DashboardScreen.route) {
            DashboardRoute(
                onTransactionClick = {
                    navController.navigate(Dialog.TransactionDialog.route)
                }
            )
        }
        composable(Screens.ProfileScreen.route) {
            DashboardRoute()
        }
        dialog(
            route = Dialog.TransactionDialog.route,
            arguments = listOf (
                navArgument(Dialog.TransactionDialog.ARG_TRANSACTION) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            TransactionDialogRoute(
                onDismiss = {
                    navController.popBackStack()
                },
                trxId = it.arguments?.getString(Dialog.TransactionDialog.ARG_TRANSACTION)
            )
        }
    }
}
