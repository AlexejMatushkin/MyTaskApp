package com.practicum.myapplication.data.cloud

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.practicum.myapplication.domain.model.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudTaskRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    private val userId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    private val tasksCollection: CollectionReference
        get() {
            val userId = auth.currentUser?.uid
                ?: throw IllegalStateException("User not authenticated")
            return firestore.collection("users").document(userId).collection("tasks")
        }

    fun syncTasksFromCloud(): Flow<List<CloudTask>> {
        return callbackFlow {
            val collection = tasksCollection
            val listener = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else {
                    val tasks = snapshot?.toObjects(CloudTask::class.java) ?: emptyList()
                    trySend(tasks)
                }
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun uploadTask(task: Task) {
        val cloudTask = task.toCloudTask(getCurrentUserId())
        tasksCollection.document(cloudTask.id).set(cloudTask)
    }

    suspend fun deleteTask(taskId: String) {
        val taskRef = tasksCollection.document(taskId)
        taskRef.update("deleted", true, "lastModified", System.currentTimeMillis())
    }

    suspend fun restoreTask(taskId: String) {
        val taskRef = tasksCollection.document(taskId)
        taskRef.update("deleted", false, "lastModified", System.currentTimeMillis())
    }

    fun getActiveTasks(): Flow<List<CloudTask>> {
        return callbackFlow {
            val query = tasksCollection.whereEqualTo("deleted", false)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else {
                    val tasks = snapshot?.toObjects(CloudTask::class.java)
                        ?.filter { !it.deleted } ?: emptyList()
                    trySend(tasks)
                }
            }
            awaitClose { listener.remove() }
        }
    }

    fun getAllTasksIncludingDeleted(): Flow<List<CloudTask>> {
        return callbackFlow {
            val listener = tasksCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else {
                    val tasks = snapshot?.toObjects(CloudTask::class.java) ?: emptyList()
                    trySend(tasks)
                }
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun updateTaskInCloud(task: Task) {
        val cloudTask = task.toCloudTask(userId)
        tasksCollection.document(cloudTask.id).set(cloudTask)
    }

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")
    }
}
