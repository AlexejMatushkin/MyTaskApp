package com.practicum.myapplication.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.practicum.myapplication.data.SyncTaskRepository
import com.practicum.myapplication.data.cloud.CloudTaskRepository
import com.practicum.myapplication.domain.repository.TaskRepository
import com.practicum.myapplication.data.local.AppDatabase
import com.practicum.myapplication.data.local.dao.CategoryDao
import com.practicum.myapplication.data.repository.RoomCategoryRepository
import com.practicum.myapplication.data.repository.RoomTaskRepository
import com.practicum.myapplication.domain.repository.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        @ApplicationContext context: Context,
        cloudRepository: CloudTaskRepository
    ): TaskRepository {
        val db = AppDatabase.getDatabase(context)
        val roomRepository = RoomTaskRepository(db.taskDao())
        return SyncTaskRepository(roomRepository, cloudRepository)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return RoomCategoryRepository(categoryDao)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }
}
