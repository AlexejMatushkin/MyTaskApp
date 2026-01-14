package com.practicum.myapplication.presentation.screen.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.practicum.myapplication.Task
import androidx.hilt.navigation.compose.hiltViewModel
import com.practicum.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    navController: NavController
) {
    val tasks by viewModel.tasks.collectAsState(emptyList())
    val categories = listOf("Общее", "Работа", "Личное", "Покупки")
    var taskIdToDelete by remember { mutableStateOf<Int?>(null) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Все") }
    val allCategories = listOf("Все") + categories

    val filteredTasks = if (selectedFilter == "Все") {
        tasks
    } else {
        tasks.filter { it.category == selectedFilter }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои задачи") },
                actions = {
                    IconButton(onClick = { navController.navigate("stats") }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bar_chart_24),
                            contentDescription = "Статистика"
                        )
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки" )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить задачу")
            }
        }
    ) { paddingValues ->
        // Весь основной контент в Column
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Фильтры категорий
            if (tasks.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allCategories) { category ->
                        FilterChip(
                            selected = selectedFilter == category,
                            onClick = { selectedFilter = category },
                            label = { Text(category) }
                        )
                    }
                }
            }

            // Список задач или пустой экран
            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tasks.isEmpty()) {
                            "Задачи пока не добавлены"
                        } else {
                            "Нет задач в категории \"$selectedFilter\""
                        }
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredTasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskToggle = { viewModel.toggleTask(task) },
                            onDelete = { taskIdToDelete = task.id },
                            onEdit = { taskToEdit = task }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }


        if (showAddDialog) {
            var title by remember { mutableStateOf("") }
            var selectedCategory by remember { mutableStateOf(categories[0]) }
            var expanded by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Новая задача") },
                text = {
                    Column {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Название задачи") },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Выпадающий список категорий
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Категория") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                viewModel.addTask(Task(
                                    id = System.currentTimeMillis().toInt(),
                                    title = title,
                                    isCompleted = false,
                                    category = selectedCategory
                                ))
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text("Добавить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Отмена")
                    }
                }
            )
        }

        if (taskIdToDelete != null) {
            AlertDialog(
                onDismissRequest = { taskIdToDelete = null },
                title = { Text("Удалить задачу?") },
                text = { Text("Вы уверены, что хотите удалить эту задачу?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val task = tasks.find { it.id == taskIdToDelete }
                            if (task != null) {
                                viewModel.deleteTask(task)
                            }
                            taskIdToDelete = null
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskIdToDelete = null }) {
                        Text("Нет")
                    }
                }
            )
        }

        taskToEdit?.let { task ->
            var title by remember { mutableStateOf(task.title) }
            var selectedCategory by remember { mutableStateOf(task.category) }
            var expanded by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { taskToEdit = null },
                title = { Text("Редактировать задачу") },
                text = {
                    Column {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Название задачи") },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Выпадающий список категорий
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Категория") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.updateTask(task.copy(
                                title = title,
                                category = selectedCategory
                            ))
                            taskToEdit = null
                        }
                    ) {
                        Text("Сохранить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskToEdit = null }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    Icons.Default.Delete, contentDescription = "Удалить задачу"
                )
            }
            IconButton(
                onClick = onEdit
            ) {
                Icon(
                    Icons.Default.Edit, contentDescription = "Редактировать"
                )
            }
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onTaskToggle() }
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = if (task.isCompleted) {
                        MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
                    } else {
                        MaterialTheme.typography.bodyMedium
                    }
                )
                Text(
                    text = "Категория: ${task.category}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "ID: ${task.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
