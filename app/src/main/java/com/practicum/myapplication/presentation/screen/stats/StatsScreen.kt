package com.practicum.myapplication.presentation.screen.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.practicum.myapplication.presentation.screen.task.TaskViewModel

@Composable
fun StatsScreen() {
    val viewModel: TaskViewModel = hiltViewModel()
    val tasks by viewModel.tasks.collectAsState(emptyList())

    val total = tasks.size
    val completed = tasks.count { it.isCompleted }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Всего задач: $total")
        Text("Всего завершено: $completed")
        Text("В работе: ${total - completed}")
    }
}