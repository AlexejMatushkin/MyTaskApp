package com.practicum.myapplication.presentation.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicum.myapplication.Task
import com.practicum.myapplication.presentation.backup.ImportSelectionScreen
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var tasksToImport by remember { mutableStateOf<List<Task>?>(null) }
    val scope = rememberCoroutineScope()


    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { viewModel.exportTasksToUri(it) }
    }

    // Launcher для выбора файла
    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            // Читаем задачи из файла
            scope.launch {
                viewModel.readTasksFromJson(it).collect { tasks ->
                    tasksToImport = tasks
                }
            }
        }
    }

    // Если есть задачи для выбора - показываем экран выбора
    if (tasksToImport != null) {
        ImportSelectionScreen(
            tasks = tasksToImport!!,
            onConfirm = { selectedTasks ->
                // Импортируем выбранные задачи
                scope.launch {
                    viewModel.importSelectedTasks(selectedTasks)
                    tasksToImport = null
                }
            },
            onDismiss = { tasksToImport = null }
        )
    } else {
        // Основной экран настроек
        SettingsContent(
            onExportClick = { exportLauncher.launch("tasks_backup.json") },
            onImportClick = { importLauncher.launch(arrayOf("application/json")) }
        )
    }
}

@Composable
private fun SettingsContent(
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onExportClick) {
            Text("Экспорт в JSON")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onImportClick) {
            Text("Импорт из JSON")
        }
    }
}
