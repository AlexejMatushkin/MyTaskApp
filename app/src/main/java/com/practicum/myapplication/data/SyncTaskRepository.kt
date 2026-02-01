package com.practicum.myapplication.data

import com.practicum.myapplication.domain.model.Task
import com.practicum.myapplication.data.cloud.CloudTaskRepository
import com.practicum.myapplication.data.cloud.toTask
import com.practicum.myapplication.data.repository.RoomTaskRepository
import com.practicum.myapplication.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncTaskRepository @Inject constructor(
    private val localRepository: RoomTaskRepository,
    private val cloudRepository: CloudTaskRepository
): TaskRepository {

    private val viewModelScope = CoroutineScope(SupervisorJob()  + Dispatchers.Main.immediate)
    private var isSyncEnabled = false

    fun enableSync() {
        isSyncEnabled = true
        syncLocalToCloud()
    }

    fun disableSync() {
        isSyncEnabled = false
    }

    override suspend fun getAllTasks(): List<Task> {
        return localRepository.getAllTasks()
    }

    override fun getAllTasksFlow(): Flow<List<Task>> {
        return if (isSyncEnabled) {
            cloudRepository.syncTasksFromCloud()
                .map { cloudTasks ->
                    val localTasks = cloudTasks.map { it.toTask() }
                    viewModelScope.launch {
                        localRepository.clearAllTasks()
                        localRepository.insertTasks(localTasks)
                    }
                    localTasks
                }
        } else {
            localRepository.getAllTasksFlow()
        }
    }

    override suspend fun addTask(task: Task) {
        localRepository.addTask(task)
        if (isSyncEnabled) {
            cloudRepository.uploadTask(task)
        }
    }

    override suspend fun updateTask(task: Task) {
        localRepository.updateTask(task)
        if (isSyncEnabled) {
            cloudRepository.updateTaskInCloud(task)
        }
    }

    override suspend fun deleteTask(task: Task) {
        localRepository.deleteTask(task)
        if (isSyncEnabled) {
            cloudRepository.deleteTask(task.id.toString())
        }
    }

    override suspend fun clearAllTasks() {
        localRepository.clearAllTasks()
        if (isSyncEnabled) {
        }
    }


    override suspend fun insertTasks(tasks: List<Task>) {
        localRepository.insertTasks(tasks)
        if (isSyncEnabled) {
            tasks.forEach { cloudRepository.uploadTask(it) }
        }
    }

    private fun syncLocalToCloud() {
        viewModelScope.launch {
            try {
                val localTasks = localRepository.getAllTasks()
                localTasks.forEach { cloudRepository.uploadTask(it) }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

}
