package com.practicum.myapplication.presentation.screens.category

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicum.myapplication.domain.model.Category
import com.practicum.myapplication.domain.presentation.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Категории") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Добавить")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onEdit = { categoryToEdit = category },
                    onDelete = { viewModel.deleteCategory(category) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Диалог добавления
    if (showAddDialog) {
        var categoryName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Новая категория") },
            text = {
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Название категории") }
                )
            },
            confirmButton = {
                Button(
                    enabled = categoryName.isNotBlank(),
                    onClick = {
                        viewModel.addCategory(categoryName.trim())
                        showAddDialog = false
                    }
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                Button(onClick = { showAddDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    // Диалог редактирования
    categoryToEdit?.let { category ->
        var categoryName by remember { mutableStateOf(category.name) }
        AlertDialog(
            onDismissRequest = { categoryToEdit = null },
            title = { Text("Редактировать категорию") },
            text = {
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Название категории") }
                )
            },
            confirmButton = {
                Button(
                    enabled = categoryName.isNotBlank(),
                    onClick = {
                        viewModel.updateCategory(category.copy(name = categoryName.trim()))
                        categoryToEdit = null
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                Button(onClick = { categoryToEdit = null }) {
                    Text("Отмена")
                }
            }
        )
    }
}
