package com.practicum.myapplication.domain.repository

import com.practicum.myapplication.Task

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}