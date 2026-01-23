package com.practicum.myapplication.data.cloud

import com.practicum.myapplication.domain.model.Task

data class CloudTask(
    val id: String = "",
    val title: String = "",
    val isCompleted: Boolean = false,
    val category: String = "Общее",
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String = "",
    val lastModified: Long = System.currentTimeMillis(),
    val deleted: Boolean = false
)

fun Task.toCloudTask(userId: String): CloudTask {
    return CloudTask(
        id = id.toString(),
        title = title,
        isCompleted = isCompleted,
        category = category,
        deleted = deleted,
        createdAt = createdAt,
        userId = userId,
        lastModified = System.currentTimeMillis()
    )
}

fun CloudTask.toTask(): Task {
    return Task(
        id = id.toIntOrNull() ?: System.currentTimeMillis().toInt(),
        title = title,
        isCompleted = isCompleted,
        category = category,
        deleted = deleted,
        createdAt = createdAt
    )
}
