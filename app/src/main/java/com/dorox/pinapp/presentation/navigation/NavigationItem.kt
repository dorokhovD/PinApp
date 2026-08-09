package com.dorox.pinapp.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.dorox.pinapp.ui.icons.ic_profile
import com.dorox.pinapp.ui.icons.ic_map
import com.dorox.pinapp.ui.icons.ic_search

sealed class NavigationItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector,

) {

    object Posts: NavigationItem(
        screen = Screen.Posts,
        label = "Поиск",
        icon = ic_search
    )

    object Map: NavigationItem(
        screen = Screen.Map,
        label = "Карта",
        icon = ic_map
    )

    object Profile: NavigationItem(
        screen = Screen.Profile,
        label = "Профиль",
        icon = ic_profile
    )

}