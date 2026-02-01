package com.practicum.myapplication.domain.model

import com.practicum.myapplication.data.local.entity.TaskEntity

data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val category: String = "Общее",
    val deleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toEntity() = TaskEntity(id,title,isCompleted, category, deleted, createdAt)
}

fun TaskEntity.toTask() = Task(id, title, isCompleted, category, deleted, createdAt)
