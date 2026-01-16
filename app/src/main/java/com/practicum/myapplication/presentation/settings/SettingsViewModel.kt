package com.practicum.myapplication.presentation.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.Task
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
    @ApplicationContext private val context: Context
) : ViewModel( ) {

    private val fileHelper = FileHelper(context)
    private val _exportResult = MutableLiveData<String>()
    val exportResult: LiveData<String> = _exportResult
    private val _importResult = MutableLiveData<String>()
    val importResult: LiveData<String> = _importResult

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