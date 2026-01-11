package com.practicum.myapplication.data.repository

import com.practicum.myapplication.Task
import com.practicum.myapplication.data.local.dao.TaskDao
import com.practicum.myapplication.domain.repository.TaskRepository
import com.practicum.myapplication.toTask

class RoomTaskRepository(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks().map { it.toTask() }
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
}
