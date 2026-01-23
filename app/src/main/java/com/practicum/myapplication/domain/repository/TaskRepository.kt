package com.practicum.myapplication.domain.repository

import com.practicum.myapplication.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasksFlow(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun clearAllTasks()
    suspend fun insertTasks(tasks: List<Task>)
    suspend fun getAllTasks(): List<Task>
}
