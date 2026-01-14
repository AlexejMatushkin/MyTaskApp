package com.practicum.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicum.myapplication.presentation.screen.stats.StatsScreen
import com.practicum.myapplication.presentation.screen.task.TaskScreen
import com.practicum.myapplication.presentation.settings.SettingsScreen
import com.practicum.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Вызывается ОДИН РАЗ - при создании Activity
        // Здесь: подключение разметки, инициализация viewModel
        setContent {
            MyApplicationTheme {
                MyAppNavHost()
            }
        }
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "tasks"
    ) {
        composable("tasks") { TaskScreen(navController = navController) }
        composable("stats") { StatsScreen() }
        composable("settings") { SettingsScreen(navController = navController) }
    }
}
