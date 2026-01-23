package com.practicum.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicum.myapplication.data.AuthManager
import com.practicum.myapplication.data.LoginScreen
import com.practicum.myapplication.data.RegisterScreen
import com.practicum.myapplication.feature.trash.TrashScreen
import com.practicum.myapplication.presentation.screens.stats.StatsScreen
import com.practicum.myapplication.presentation.screens.task.TaskScreen
import com.practicum.myapplication.presentation.screens.settings.SettingsScreen
import com.practicum.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Вызывается ОДИН РАЗ - при создании Activity
        // Здесь: подключение разметки, инициализация viewModel
        setContent {
            MyApplicationTheme {
                MyAppNavHost(authManager.isSignedIn())
            }
        }
    }
}

@Composable
fun MyAppNavHost(isAuthenticated: Boolean,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "tasks" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("tasks") }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("tasks") {
            TaskScreen(navController = navController)
        }
        composable("stats") {
            StatsScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }

        composable("trash") {
            TrashScreen(navController = navController)
        }
    }
}
