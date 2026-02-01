package com.practicum.myapplication.presentation.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicum.myapplication.domain.model.Task
import com.practicum.myapplication.presentation.backup.ImportSelectionScreen
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var tasksToImport by remember { mutableStateOf<List<Task>?>(null) }
    val scope = rememberCoroutineScope()
    val authState by viewModel.authState.observeAsState(false)
    val isSyncEnabled by viewModel.isSyncEnabled.observeAsState(false)

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { viewModel.exportTasksToUri(it) }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            scope.launch {
                viewModel.readTasksFromJson(it).collect { tasks ->
                    tasksToImport = tasks
                }
            }
        }
    }

    if (tasksToImport != null) {
        ImportSelectionScreen(
            tasks = tasksToImport!!,
            onConfirm = { selectedTasks ->
                scope.launch {
                    viewModel.importSelectedTasks(selectedTasks)
                    tasksToImport = null
                }
            },
            onDismiss = { tasksToImport = null }
        )
    } else {
        SettingsContent(
            isSyncEnabled = isSyncEnabled,
            onSyncToggle = { enabled ->
                if (enabled && !authState) {
                    // Можно добавить обработку ошибки
                    return@SettingsContent
                }
                viewModel.toggleSync(enabled)
            },
            onExportClick = { exportLauncher.launch("tasks_backup.json") },
            onImportClick = { importLauncher.launch(arrayOf("application/json")) }
        )
    }
}

@Composable
private fun SettingsContent(
    isSyncEnabled: Boolean,
    onSyncToggle: (Boolean) -> Unit,
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

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Синхронизация с облаком")
            Switch(
                checked = isSyncEnabled,
                onCheckedChange = onSyncToggle
            )
        }
    }
}
