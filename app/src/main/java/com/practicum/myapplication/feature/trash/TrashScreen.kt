package com.practicum.myapplication.feature.trash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicum.myapplication.domain.model.Task

// TrashScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    navController: NavController,
    viewModel: TrashViewModel = hiltViewModel()
) {
    val deletedTasks by viewModel.deletedTasks.collectAsState()
    var taskToDeletePermanently by remember { mutableStateOf<Task?>(null) }

    taskToDeletePermanently?.let { task ->
        AlertDialog(
            onDismissRequest = { taskToDeletePermanently = null},
            title = { Text("Удалить навсегда?") },
            text = { Text("Задача будет удалена без возможности восстановления.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.permanentlyDeleteTask(task)
                        taskToDeletePermanently = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToDeletePermanently = null }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Корзина") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (deletedTasks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Корзина пуста")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                items(deletedTasks) { task ->
                    TrashTaskItem(
                        task = task,
                        onRestore = { viewModel.restoreTask(task) },
                        onDelete = { taskToDeletePermanently = task }
                    )
                }
            }
        }
    }
}
