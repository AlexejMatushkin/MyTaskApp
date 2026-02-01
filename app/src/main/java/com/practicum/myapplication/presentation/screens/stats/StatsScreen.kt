package com.practicum.myapplication.presentation.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicum.myapplication.presentation.screens.task.TaskViewModel
import kotlin.math.roundToInt

@Composable
fun StatsScreen(
    navController: NavController,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
       item {
           Card(
               modifier = Modifier.fillMaxWidth()
           ) {
               Column(
                   modifier = Modifier.padding(16.dp)
               ) {
                   Text("Общая статистика", style = MaterialTheme.typography.headlineSmall)
                   Spacer(modifier = Modifier.height(16.dp))
                   Text("Всего задач: ${stats.totalTasks}")
                   Text("Завершено: ${stats.completedTasks}")
                   Text("Прогресс: ${stats.completionRate.roundToInt()}%")
               }
           }
       }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            DailyStatsChart(stats.dailyStats)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Прогресс по категориям", style = MaterialTheme.typography.headlineSmall)
        }

        items(stats.categoryStats.toList()) { (category, categoryStats) ->
            Spacer(modifier = Modifier.height(8.dp))
            CategoryProgressItem(
                categoryName = category,
                stats = categoryStats
            )
        }
    }
}