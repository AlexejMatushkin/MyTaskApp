package com.practicum.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val  id: Int,
    val title: String,
    val isCompleted: Boolean,
    val category: String,
    val createdAt: Long = System.currentTimeMillis()
)
