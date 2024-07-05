package com.hello.ebookreader.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hello.ebookreader.presentation.BooksByCategory
import com.hello.ebookreader.presentation.TabLayout


@Composable
fun NavGraphHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItem.HomeScreen) {
        composable<NavigationItem.HomeScreen> {
            TabLayout(navController)
        }

        composable<NavigationItem.BooksByCategory> {
            val category = it.toRoute<NavigationItem.BooksByCategory>()
            BooksByCategory(category = category.category, navController = navController)
        }
    }
}