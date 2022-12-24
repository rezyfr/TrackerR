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

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.rezyfr.trackerr.feature.homescreen.ui.TransactionScreen
import io.rezyfr.trackerr.ui.auth.LoginScreen

@Composable
fun TrNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(modifier = Modifier.padding(16.dp), onLogin = {
                navController.navigate(Screens.TransactionScreen.route) {
                    launchSingleTop = true
                }
            })
        }
        composable(Screens.TransactionScreen.route) {
            TransactionScreen(
                modifier = Modifier.padding(
                    16.dp
                )
            )
        }
    }
}
