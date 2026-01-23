package com.practicum.myapplication.data.json

import com.practicum.myapplication.domain.model.Task

data class TaskJson(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val category: String,
    val deleted: Boolean
)

fun Task.toJson() = TaskJson(id, title, isCompleted, category, deleted)
fun TaskJson.toTask() = Task(id, title, isCompleted, category, deleted)
