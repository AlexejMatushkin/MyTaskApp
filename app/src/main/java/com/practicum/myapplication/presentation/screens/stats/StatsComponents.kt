package com.practicum.myapplication.presentation.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CategoryProgressItem(
    categoryName: String,
    stats: CategoryStats,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$categoryName (${stats.completed}/${stats.total})")
            Text("${stats.completionRate.roundToInt()}%", style = MaterialTheme.typography.labelSmall)
        }
        LinearProgressIndicator(
            progress = { stats.completionRate / 100f },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DailyStatsChart(
    dailyStats: Map< String, Int>,
    modifier: Modifier = Modifier
) {
    val maxCount = dailyStats.values.maxOrNull() ?: 1
    val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    val counts = days.map { dailyStats.getOrDefault(it, 0) }

    Column( modifier =  modifier) {
        Text("Задачи за последние 7 дней", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEachIndexed { index, day ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height((counts[index] * 100f / maxCount).dp)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(day, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}