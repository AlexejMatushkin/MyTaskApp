package com.practicum.myapplication.presentation.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Task
import com.practicum.myapplication.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks = repository.getAllTasksFlow()
        .map { tasks -> tasks.filter { !it.deleted } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val trashedTask = task.copy(deleted = true)
            repository.updateTask(trashedTask)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun moveTasksToGeneral(oldCategory: String) {
        viewModelScope.launch {
            // Получить все задачи из старой категории
            val tasksToMove = repository.getAllTasks()
                .filter { it.category == oldCategory }

            // Обновить их категорию на "Общее"
            tasksToMove.forEach { task ->
                repository.updateTask(task.copy(category = "Общее"))
            }
        }
    }
}
