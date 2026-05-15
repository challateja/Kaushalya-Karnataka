package com.example.kaushalya_karnataka.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Main : Screen("main?tab={tab}") {
        fun createRoute(tab: Int = 0) = "main?tab=$tab"
    }
    object WorkerDetail : Screen("worker_detail/{workerId}") {
        fun createRoute(workerId: String) = "worker_detail/$workerId"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    val repository = remember { WorkerRepository() }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { fadeIn(tween(400)) },
        exitTransition = { fadeOut(tween(400)) }
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDiscovery = {
                    navController.navigate(Screen.Main.createRoute(0)) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToProfileSetup = {
                    navController.navigate(Screen.Main.createRoute(1)) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                repository = repository
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Main.route,
            arguments = listOf(navArgument("tab") { 
                type = NavType.IntType
                defaultValue = 0 
            })
        ) { backStackEntry ->
            val initialTab = backStackEntry.arguments?.getInt("tab") ?: 0
            MainScreen(
                initialTab = initialTab,
                onWorkerClick = { workerId ->
                    navController.navigate(Screen.WorkerDetail.createRoute(workerId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                repository = repository
            )
        }

        composable(
            route = Screen.WorkerDetail.route,
            arguments = listOf(navArgument("workerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workerId = backStackEntry.arguments?.getString("workerId") ?: return@composable
            WorkerDetailScreen(
                workerId = workerId,
                onBackClick = { navController.popBackStack() },
                repository = repository
            )
        }
    }
}
