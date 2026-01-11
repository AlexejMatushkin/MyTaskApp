package com.practicum.myapplication.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practicum.myapplication.domain.repository.TaskRepository
import com.practicum.myapplication.data.local.AppDatabase
import com.practicum.myapplication.data.repository.RoomTaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(@ApplicationContext context: Context): TaskRepository {
        val db = AppDatabase.getDatabase(context)
        return RoomTaskRepository(db.taskDao())
    }
}
