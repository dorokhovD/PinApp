package com.dorox.pinapp.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dorox.pinapp.R
import com.dorox.pinapp.presentation.navigation.NavGraph
import com.dorox.pinapp.presentation.navigation.NavigationItem
import com.dorox.pinapp.presentation.navigation.Screen
import com.dorox.pinapp.presentation.screen.creation.CreatePostScreen
import com.dorox.pinapp.presentation.screen.map.MapScreen
import com.dorox.pinapp.presentation.screen.posts.PostsScreen
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.Gray100
import com.dorox.pinapp.ui.theme.Orange100
import com.dorox.pinapp.ui.theme.Orange200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute == Screen.CreatePost.route) {
                CreateScreenTopAppBar(
                    onClick = {
                        navController.popBackStack()
                    }
                )
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Posts.route) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.CreatePost.route)
                    },
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add_ad),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = currentRoute,
                backStackEntry = backStackEntry
            )
        }
    ) { innerPadding ->

        NavGraph(
            //modifier = Modifier.padding(innerPadding),
            navHostController = navController,
            postsScreenContent = {
                PostsScreen(
                    innerPadding = innerPadding,
                )
            },
            mapScreenContent = {
                MapScreen()
            },
            createPostScreenContent = {
                CreatePostScreen(
                    innerPadding = innerPadding,
                    onFinished = {
                        navController.popBackStack()
                    },

                )
            },
            profileScreenContent = {

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateScreenTopAppBar(
    onClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.shadow(4.dp),
        title = {
            Text(
                text = "Новое объявление",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onClick() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange100,
        )
    )
}

@Composable
private fun AppBottomBar(
    navController: NavHostController,
    currentRoute: String?,
    backStackEntry: NavBackStackEntry?
) {

    AnimatedVisibility(
        visible = currentRoute in listOf(Screen.Posts.route, Screen.Map.route, Screen.Profile.route),
    ) {
        NavigationBar(
            modifier = Modifier
                .shadow(4.dp),
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
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

                NavigationBarItem(
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
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = item.label)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = Orange200,
                        selectedIconColor = Orange200,
                        unselectedIconColor = Gray100,
                        unselectedTextColor = Gray100,
                        indicatorColor = Orange100
                    )
                )
            }
        }
    }
}