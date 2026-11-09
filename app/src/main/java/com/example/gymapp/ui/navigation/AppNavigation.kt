package com.example.gymapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymapp.di.AppContainer
import com.example.gymapp.di.createViewModelFactory
import com.example.gymapp.ui.auth.LoginScreen
import com.example.gymapp.ui.auth.RegisterScreen
import com.example.gymapp.ui.detail.DetailScreen
import com.example.gymapp.ui.form.RutinaFormScreen
import com.example.gymapp.ui.home.HomeScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val DETAIL = "detail/{rutinaId}"
    const val FORM = "form?rutinaId={rutinaId}"

    fun detail(id: String): String = "detail/$id"

    fun form(id: String? = null): String =
        if (id.isNullOrBlank()) "form" else "form?rutinaId=$id"
}

@Composable
fun GymApp(container: AppContainer) {
    val navController = rememberNavController()
    val factory: ViewModelProvider.Factory = remember(container) {
        createViewModelFactory(container)
    }
    val startDestination = remember {
        if (container.authRepository.isLoggedIn()) Routes.HOME else Routes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                factory = factory,
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                factory = factory,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                factory = factory,
                onAddRutina = { navController.navigate(Routes.form()) },
                onEditRutina = { id -> navController.navigate(Routes.form(id)) },
                onDetailRutina = { id -> navController.navigate(Routes.detail(id)) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("rutinaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId").orEmpty()
            DetailScreen(
                rutinaId = rutinaId,
                factory = factory,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.FORM,
            arguments = listOf(
                navArgument("rutinaId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")
            RutinaFormScreen(
                rutinaId = rutinaId,
                factory = factory,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
