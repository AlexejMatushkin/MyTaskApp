package com.practicum.myapplication.data.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.myapplication.domain.model.Task
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream

class FileHelper(private val context: Context) {
    private val gson = Gson()
    private val taskListType = object  : TypeToken<List<TaskJson>>() {}.type

     fun exportTasks(tasks: List<Task>, outputStream: OutputStream) {
        val taskJsons = tasks.map { it.toJson() }
        outputStream.writer().use { writer ->
            gson.toJson(taskJsons, taskListType, writer)
        }
    }

   fun importTasks(inputStream: InputStream): List<Task> {
        val reader = InputStreamReader(inputStream)
        val taskJsons = gson.fromJson(reader, taskListType) as List<TaskJson>
        return taskJsons.map { it.toTask() }
    }
}
