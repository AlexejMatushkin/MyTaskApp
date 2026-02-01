package com.practicum.myapplication.feature.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Task
import com.practicum.myapplication.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val deletedTasks = repository.getAllTasksFlow()
        .map { tasks ->
            tasks.filter { it.deleted }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun restoreTask(task: Task) {
        viewModelScope.launch {
            val restoredTask = task.copy(deleted = false)
            repository.updateTask(restoredTask)
        }
    }

    fun permanentlyDeleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}