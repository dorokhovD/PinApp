package com.dorox.courseapp.presentation.main

import androidx.compose.ui.graphics.vector.ImageVector
import com.dorox.courseapp.ui.icons.ic_account
import com.dorox.courseapp.ui.icons.ic_favorite
import com.dorox.courseapp.ui.icons.ic_home

sealed class NavigationItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) {

    object Home: NavigationItem(
        screen = Screen.Home,
        title = "Главная",
        icon = ic_home
    )

    object Favorite: NavigationItem(
        screen = Screen.Favorite,
        title = "Избранное",
        icon = ic_favorite
    )

    object Account: NavigationItem(
        screen = Screen.Account,
        title = "Аккаунт",
        icon = ic_account
    )

}