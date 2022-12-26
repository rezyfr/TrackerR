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

package io.rezyfr.trackerr.core.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Light default theme color scheme
 */
private val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    outline = PurpleGray50
)

/**
 * Dark default theme color scheme
 */
private val DarkDefaultColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue10,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkBlue10,
    onBackground = DarkBlue90,
    surface = DarkBlue10,
    onSurface = DarkBlue90,
    outline = DarkBlue80
)

/**
 * Light Android theme color scheme
 */
private val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    outline = GreenGray50
)

/**
 * Dark Android theme color scheme
 */
private val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    outline = GreenGray60
)
@Composable
fun TrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    androidTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkDefaultColorScheme
    val backgroundScheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp
    )
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//        androidTheme && darkTheme -> DarkAndroidColorScheme
//        androidTheme -> LightAndroidColorScheme
//        darkTheme -> DarkDefaultColorScheme
//        else -> LightDefaultColorScheme
//    }
//    val backgroundTheme = when {
//        androidTheme && darkTheme -> BackgroundTheme(
//            color = Color.Black
//        )
//        androidTheme -> BackgroundTheme(
//            color = DarkGreenGray95
//        )
//        darkTheme -> BackgroundTheme(
//            color = colorScheme.surface,
//            tonalElevation = 2.dp
//        )
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> BackgroundTheme(
//            color = colorScheme.surface,
//            tonalElevation = 2.dp,
//            primaryGradientColor = colorScheme.primary.lighten(0.95f),
//            secondaryGradientColor = colorScheme.secondary.lighten(0.95f),
//            tertiaryGradientColor = colorScheme.tertiary.lighten(0.95f),
//            neutralGradientColor = colorScheme.surface.lighten(0.95f)
//        )
//        else -> BackgroundTheme(
//            color = colorScheme.surface,
//            tonalElevation = 2.dp,
//            primaryGradientColor = Purple95,
//            secondaryGradientColor = Orange95,
//            tertiaryGradientColor = Blue95,
//            neutralGradientColor = DarkPurpleGray95
//        )
//    }

    CompositionLocalProvider(LocalBackgroundTheme provides backgroundScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MainTypography,
            content = content
        )
    }
}
