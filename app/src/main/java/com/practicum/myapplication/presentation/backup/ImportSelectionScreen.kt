package com.practicum.myapplication.presentation.backup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.practicum.myapplication.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportSelectionScreen(
    tasks: List<Task>,
    onConfirm: (List<Task>) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedTaskIds = remember { mutableStateListOf<Int>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выбор задач") },
                actions = {
                    IconButton(onClick = {
                        val selectedTasks = tasks.filter { it.id in selectedTaskIds }
                        onConfirm(selectedTasks)
                    }) {
                        Icon(Icons.Default.Done, contentDescription = "Импортировать")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(tasks) { task ->
                val isSelected = task.id in selectedTaskIds

                ImportTaskItem(
                    task = task,
                    isSelected = isSelected,
                    onToggleSelection = {
                        if (isSelected) {
                            selectedTaskIds.remove(task.id)
                        } else {
                            selectedTaskIds.add(task.id)
                        }
                    }
                )
            }
        }
    }
}