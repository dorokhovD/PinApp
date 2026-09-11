package com.dorox.pinapp.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dorox.pinapp.presentation.navigation.NavGraph
import com.dorox.pinapp.presentation.navigation.NavigationItem
import com.dorox.pinapp.presentation.navigation.Screen
import com.dorox.pinapp.presentation.screen.auth.AuthScreen
import com.dorox.pinapp.presentation.screen.creation.CreatePostScreen
import com.dorox.pinapp.presentation.screen.map.MapScreen
import com.dorox.pinapp.presentation.screen.posts.PostsScreen
import com.dorox.pinapp.presentation.screen.profile.ProfileScreen
import com.dorox.pinapp.ui.theme.Gray550
import com.dorox.pinapp.ui.theme.Orange10
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.Orange50
import com.dorox.pinapp.ui.theme.White100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute,
                backStackEntry = backStackEntry
            )
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->

        NavGraph(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(WindowInsets.navigationBars),
            navHostController = navController,
            postsScreenContent = {
                PostsScreen(
                    onCreatePostClick = {
                        navController.navigate(Screen.CreatePost.route)
                    },
                )
            },
            mapScreenContent = {
                MapScreen()
            },
            createPostScreenContent = {
                CreatePostScreen(
                    onFinished = {
                        navController.popBackStack()
                    },

                    )
            },
            profileScreenContent = {
                ProfileScreen(
                    onRegisterButtonClick = {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Profile.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            },
            authScreenContent = {
                AuthScreen {

                }
            }
        )
    }
}



@Composable
private fun AppBottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,
    backStackEntry: NavBackStackEntry?
) {
    AnimatedVisibility(
        visible = currentRoute in listOf(Screen.Posts.route, Screen.Map.route, Screen.Profile.route),
    ) {

        ShortNavigationBar(
            modifier = Modifier.shadow(4.dp),
            containerColor = White100

        ) {
            val navItems = listOf(
                NavigationItem.Posts,
                NavigationItem.Map,
                NavigationItem.Profile,
            )

            navItems.forEach { item ->

                // переменная вернет true если в иерархии любой из элементов списка совпадает с выбранным элементом
                val selected = backStackEntry?.destination?.hierarchy?.any {
                    it.route == item.screen.route
                } ?: false

                ShortNavigationBarItem(
                    modifier = Modifier.padding(vertical = 0.dp),
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                    },
                    icon = {
                        Icon(
                            modifier = Modifier,
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = ShortNavigationBarItemDefaults.colors(
                        selectedTextColor = Orange200,
                        selectedIconColor = Orange200,
                        unselectedIconColor = Gray550,
                        unselectedTextColor = Gray550,
                        selectedIndicatorColor = Orange50
                    ),
                )
            }
        }
    }
}