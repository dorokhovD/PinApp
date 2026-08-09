package com.dorox.pinapp.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dorox.pinapp.presentation.screen.posts.PostsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    postsScreenContent: @Composable () -> Unit,
    mapScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    createPostScreenContent: @Composable () -> Unit
) {


    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screen.Posts.route
    ) {

        composable(
            route = Screen.Posts.route,
            enterTransition = { fadeIn() + slideInHorizontally() },
            exitTransition = { fadeOut() + slideOutHorizontally() }
        ) {
            postsScreenContent()
        }

        composable(
            route = Screen.CreatePost.route,
            enterTransition = { fadeIn()},
            exitTransition = { fadeOut()}
        ) {
            createPostScreenContent()
        }


        composable(route = Screen.Map.route,
            enterTransition = { fadeIn() + slideInHorizontally() },
            exitTransition = { fadeOut() + slideOutHorizontally() }
        ) {
            mapScreenContent()
        }

        composable(route = Screen.Profile.route,
            enterTransition = { fadeIn() + slideInHorizontally() },
            exitTransition = { fadeOut() + slideOutHorizontally() }
            ) {
            profileScreenContent()
        }
    }
}

sealed class Screen(val route: String) {
    object Posts: Screen("posts")
    object CreatePost: Screen("create_post")
    object Map: Screen("map")
    object Profile: Screen("profile")
}