package io.rezyfr.trackerr.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.spec.NavGraphSpec
import io.rezyfr.trackerr.core.ui.DarkBlue20
import io.rezyfr.trackerr.core.ui.Purple95
import io.rezyfr.trackerr.core.ui.icon.Icon


@Composable
fun TrBottomBar(
    destinations: List<BottomNavDestination>,
    selectedNavigation: NavGraphSpec,
    onNavigationSelected: (NavGraphSpec) -> Unit,
    modifier: Modifier = Modifier
) {
    Box() {
        TrNavigationBar(
            modifier = modifier
        ) {
            destinations.forEach { destination ->
                TrNavigationBarItem(
                    selected = selectedNavigation == destination.screen,
                    onClick = { onNavigationSelected(destination.screen) },
                    icon = {
                        val icon = if (selectedNavigation == destination.screen) {
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
}

/**
 * Navigation bar item with icon and label content slots. Wraps Material 3
 * [NavigationBarItem].
 *
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param modifier Modifier to be applied to this item.
 * @param selectedIcon The item icon content when selected.
 * @param enabled controls the enabled state of this item. When `false`, this item will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The item text label content.
 * @param alwaysShowLabel Whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 */
@Composable
fun RowScope.TrNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = TrNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = TrNavigationDefaults.navigationContentColor(),
            selectedTextColor = TrNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = TrNavigationDefaults.navigationContentColor(),
            indicatorColor = TrNavigationDefaults.navigationIndicatorColor()
        )
    )
}

@Composable
fun TrNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    NavigationBar(
        modifier = modifier,
        contentColor = TrNavigationDefaults.navigationContentColor(),
        containerColor = Purple95.copy(alpha = 0.5f),
        tonalElevation = 0.dp,
        content = content
    )
}

object TrNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onBackground

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
