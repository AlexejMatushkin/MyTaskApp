package com.practicum.myapplication.presentation.screens.task

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.practicum.myapplication.domain.model.Task
import androidx.hilt.navigation.compose.hiltViewModel
import com.practicum.myapplication.R
import com.practicum.myapplication.domain.presentation.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    navController: NavController
) {
    val tasks by viewModel.tasks.collectAsState(emptyList())
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val categories by categoryViewModel.categories.collectAsState(listOf())
    val categoryNames = categories.map { it.name }
    var selectedCategory by remember { mutableStateOf("") }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Все") }
    val allCategories = listOf("Все") + categories.map { it.name }
    var showCreateCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

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
                    IconButton(onClick = { navController.navigate("trash") }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Корзина"
                        )
                    }
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
                            onToggle = { viewModel.toggleTask(task) },
                            onDelete = { taskToDelete  = task },
                            onEdit = { taskToEdit = task },
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }


        if (showAddDialog) {

            LaunchedEffect(categories) {
                if (selectedCategory.isEmpty() || !categoryNames.contains(selectedCategory)) {
                    selectedCategory = categories.firstOrNull()?.name ?: "Общее"
                }
            }

            var title by remember { mutableStateOf("") }
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
                                // Существующие категории
                                categoryNames.filter { it != "Общее" }.forEach { categoryName -> // Защитим базовую категорию
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(categoryName)
                                                IconButton(
                                                    onClick = {
                                                        expanded = false
                                                        showDeleteCategoryDialog = true
                                                        categoryToDelete = categoryName
                                                    },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Удалить категорию",
                                                        tint = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            selectedCategory = categoryName
                                            expanded = false
                                        }
                                    )
                                }

                                // Разделитель и опция создания новой
                                Divider()
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Icon(Icons.Default.Add, contentDescription = null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Создать новую...")
                                        }
                                    },
                                    onClick = {
                                        expanded = false
                                        showCreateCategoryDialog = true
                                    }
                                )
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

        if (showCreateCategoryDialog) {
            AlertDialog(
                onDismissRequest = {
                    showCreateCategoryDialog = false
                    newCategoryName = ""
                },
                title = { Text("Новая категория") },
                text = {
                    TextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Название категории") },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                },
                confirmButton = {
                    TextButton(
                        enabled = newCategoryName.isNotBlank(),
                        onClick = {
                            val categoryName = newCategoryName.trim()
                            categoryViewModel.addCategory(categoryName)
                            selectedCategory = categoryName
                            showCreateCategoryDialog = false
                            newCategoryName = ""
                        }
                    ) {
                        Text("Создать")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showCreateCategoryDialog = false
                        newCategoryName = ""
                    }) {
                        Text("Отмена")
                    }
                }
            )
        }

        if (showDeleteCategoryDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteCategoryDialog = false
                    categoryToDelete = ""
                },
                title = { Text("Удалить категорию?") },
                text = {
                    Text("Все задачи в категории \"$categoryToDelete\" будут перемещены в \"Общее\".")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Найти категорию по имени и удалить
                            val categoryToRemove = categories.find { it.name == categoryToDelete }
                            if (categoryToRemove != null) {
                                categoryViewModel.deleteCategory(categoryToRemove)
                                // Переместить задачи в "Общее"
                                viewModel.moveTasksToGeneral(categoryToDelete)
                                selectedCategory = "Общее"
                            }
                            showDeleteCategoryDialog = false
                            categoryToDelete = ""
                        }
                    ) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteCategoryDialog = false
                        categoryToDelete = ""
                    }) {
                        Text("Отмена")
                    }
                }
            )
        }

        taskToDelete?.let { task ->
            AlertDialog(
                onDismissRequest = { taskToDelete = null },
                title = { Text("Удалить задачу?") },
                text = { Text("Задача будет перемещена в корзину.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTask(task)
                            taskToDelete = null
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskToDelete = null }) {
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
                                        text = { Text(category.name) },
                                        onClick = {
                                            selectedCategory = category.name
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
    onToggle: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    onRestore: (() -> Unit)? = null,
    isSelected: Boolean = false,
    onToggleSelection: (() -> Unit)? = null,
    modifier: Modifier
) {
    Card(
        modifier = modifier
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
                onClick = { onDelete?.invoke() },
                enabled = onDelete != null
            ) {
                Icon(
                    Icons.Default.Delete, contentDescription = "Удалить задачу"
                )
            }
            IconButton(
                onClick = { onEdit?.invoke() },
                enabled = onEdit != null
            ) {
                Icon(
                    Icons.Default.Edit, contentDescription = "Редактировать"
                )
            }
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle?.invoke() }
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
