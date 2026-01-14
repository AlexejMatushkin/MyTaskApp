package com.practicum.myapplication.data.repository

import androidx.room.Query
import com.practicum.myapplication.Task
import com.practicum.myapplication.data.local.dao.TaskDao
import com.practicum.myapplication.domain.repository.TaskRepository
import com.practicum.myapplication.toTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomTaskRepository (private val taskDao: TaskDao) : TaskRepository {
    override fun getAllTasksFlow(): Flow<List<Task>> {
        return taskDao.getAllTasksFlow()
            .map { entities -> entities.map { it.toTask() } }
    }

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override suspend fun clearAllTasks() = taskDao.clearAllTasks()

    override suspend fun insertTasks(tasks: List<Task>) {
        taskDao.insertTasks(tasks.map { it.toEntity() })
    }
}
