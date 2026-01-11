package com.practicum.myapplication

import com.practicum.myapplication.data.local.entity.TaskEntity

data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val category: String = "Общее"
) {
    fun toEntity() = TaskEntity(id,title,isCompleted, category)
}

fun TaskEntity.toTask() = Task(id, title, isCompleted, category)
