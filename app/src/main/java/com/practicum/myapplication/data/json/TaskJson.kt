package com.practicum.myapplication.data.json

import com.practicum.myapplication.Task

data class TaskJson(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val category: String
)

fun Task.toJson() = TaskJson(id, title, isCompleted, category)
fun TaskJson.toTask() = Task(id, title, isCompleted, category)
