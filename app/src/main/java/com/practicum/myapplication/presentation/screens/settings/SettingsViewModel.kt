package com.practicum.myapplication.presentation.screens.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Task
import com.practicum.myapplication.data.AuthManager
import com.practicum.myapplication.data.SyncTaskRepository
import com.practicum.myapplication.data.json.FileHelper
import javax.inject.Inject
import com.practicum.myapplication.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val authManager: AuthManager,
    @ApplicationContext private val context: Context
) : ViewModel( ) {

    private val fileHelper = FileHelper(context)
    private val _exportResult = MutableLiveData<String>()
    val exportResult: LiveData<String> = _exportResult
    private val _importResult = MutableLiveData<String>()
    val importResult: LiveData<String> = _importResult
    private val _authState = MutableLiveData<Boolean>()
    val authState: LiveData<Boolean> = _authState
    private val _isSyncEnabled = MutableLiveData<Boolean>(false)
    val isSyncEnabled: LiveData<Boolean> = _isSyncEnabled

    private val preferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    private val SYNC_ENABLED_KEY = "sync_enabled"

    init {
        _authState.value = authManager.isSignedIn()

        val savedSyncEnabled = preferences.getBoolean(SYNC_ENABLED_KEY, false)
        if (savedSyncEnabled && authManager.isSignedIn()) {
            enableSyncIfNeeded()
        } else {
            _isSyncEnabled.value = false
        }
    }


    fun toggleSync(enabled: Boolean) {
        if (enabled && !authManager.isSignedIn()) {
            return
        }

        if (repository is SyncTaskRepository) {
            if (enabled) {
                repository.enableSync()
            } else {
                repository.disableSync()
            }
            _isSyncEnabled.value = enabled

            preferences.edit().putBoolean(SYNC_ENABLED_KEY, enabled).apply()
        }
    }

    private fun enableSyncIfNeeded() {
        if (repository is SyncTaskRepository) {
            repository.enableSync()
            _isSyncEnabled.value = true
        }
    }

    fun exportTasksToUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val tasks = repository.getAllTasksFlow().first()
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    fileHelper.exportTasks(tasks, outputStream)
                }
                _exportResult.value = "Экспорт завершён"
            } catch (e: Exception) {
                _exportResult.value = "Ошибка экспорта: ${e.message}"
            }
        }
    }

    fun importTasks(uri: Uri) {
        viewModelScope.launch {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val tasks = fileHelper.importTasks(inputStream)
                    repository.clearAllTasks()
                    repository.insertTasks(tasks)
                    _importResult.value = "Импортировано ${tasks.size} задач"
                }
            } catch (e: Exception) {
                _importResult.value = "Ошибка импорта: ${e.message}"
            }
        }
    }

    fun  readTasksFromJson(uri: Uri) : Flow<List<Task>> = flow {
        try {
            context.contentResolver.openInputStream(uri)?.use {  inputStream ->
                val tasks = fileHelper.importTasks(inputStream)
                emit(tasks)
            }
        } catch (e: Exception) {

        }
    }

    fun importSelectedTasks(tasks: List<Task>) {
        viewModelScope.launch {
            try {
                repository.insertTasks(tasks)
                // Room Flow автоматически обновит UI в TaskScreen
            } catch (e: Exception) {
                // Можно добавить обработку ошибок
            }
        }
    }
}