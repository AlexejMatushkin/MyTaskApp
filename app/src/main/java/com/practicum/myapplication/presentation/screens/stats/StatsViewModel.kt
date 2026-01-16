package com.practicum.myapplication.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.Task
import com.practicum.myapplication.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val stats = repository.getAllTasksFlow()
        .map { tasks -> calculateStats(tasks) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Stats()
        )

    private fun calculateStats( tasks: List<Task>): Stats {
        val completed = tasks.count { it.isCompleted }
        val total = tasks.size
        val completionRate = if (total > 0) (completed * 100f / total) else 0f

        val categoryStats = tasks.groupBy { it.category }
            .mapValues { (_, categoryTasks) ->
                val catCompleted = categoryTasks.count { it.isCompleted }
                val catTotal = categoryTasks.size
                CategoryStats(
                    total = catTotal,
                    completed = catCompleted,
                    completionRate = if (catTotal > 0) (catCompleted * 100f / catTotal) else 0f
                )
            }

        val dailyStats = calculateDailyStats(tasks)

        return Stats(
            totalTasks = total,
            completedTasks = completed,
            completionRate = completionRate,
            categoryStats = categoryStats,
            dailyStats = dailyStats
        )
    }

    private fun calculateDailyStats(tasks: List<Task>): Map<String, Int> {
        val now = System.currentTimeMillis()
        val sevenDaysAgo = now - ( 7 * 24 * 60 * 60 * 1000)

        return tasks
            .filter { it.createdAt >= sevenDaysAgo }
            .groupBy {
                val date = java.util.Date(it.createdAt)
                val day = java.text.SimpleDateFormat("E", java.util.Locale.getDefault()).format(date)
                day
            }
            .mapValues { it.value.size }
    }
}

data class Stats(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val completionRate: Float = 0f,
    val categoryStats: Map<String, CategoryStats> = emptyMap(),
    val dailyStats: Map<String, Int> = emptyMap()
)

data class CategoryStats(
    val total: Int,
    val completed: Int,
    val completionRate: Float
)
