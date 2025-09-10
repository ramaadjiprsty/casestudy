package com.rama.casestudy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rama.casestudy.ui.features.user_detail.UserDetailScreen
import com.rama.casestudy.ui.features.user_list.UserListScreen

sealed class Screen(val route: String) {
    object UserList : Screen("user_list_screen")
    object UserDetail : Screen("user_detail_screen/{userId}") {
        fun createRoute(userId: Int) = "user_detail_screen/$userId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.UserList.route
    ) {
        composable(route = Screen.UserList.route) {
            UserListScreen(navController = navController)
        }
        composable(
            route = Screen.UserDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            UserDetailScreen(navController = navController)
        }
    }
}