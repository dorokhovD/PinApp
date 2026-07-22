package com.dorox.courseapp.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dorox.courseapp.presentation.favorite.FavoriteScreen
import com.dorox.courseapp.presentation.login.LoginScreen
import com.dorox.courseapp.ui.theme.Gray200
import com.dorox.courseapp.ui.theme.Gray400
import com.dorox.courseapp.ui.theme.Green100
import com.dorox.courseapp.ui.theme.White100

@Composable
fun CourseMainScreen() {

    val navController = rememberNavController()

    val viewModel: CourseViewModel = hiltViewModel()

    val navItems = listOf(
        NavigationItem.Home,
        NavigationItem.Favorite,
        NavigationItem.Account,
    )

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            if (currentRoute in navItems.map { navItem ->
                navItem.screen.route
            }) {
                NavigationBar(
                    containerColor = Gray400
                ) {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null
                                )
                            },
                            label =  {
                                Text(text = item.title)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedTextColor = Green100,
                                selectedIconColor = Green100,
                                unselectedIconColor = White100,
                                unselectedTextColor = White100,
                                indicatorColor = Gray200
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(
                    paddingValues = paddingValues,
                    viewModel = viewModel
                )
            }
            composable(route = Screen.Favorite.route) {
                FavoriteScreen(
                    paddingValues = paddingValues,
                    viewModel = viewModel
                )
            }
            composable(route = Screen.Account.route) {
                Scaffold {
                    Text(
                        modifier = Modifier.padding(it),
                        text = "Аккаунт"
                    )
                }
            }
            composable(route = Screen.Login.route) {
                LoginScreen(
                    onClickLoginButton = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Home: Screen("home")
    object Favorite: Screen("favorite")
    object Account: Screen("account")
}