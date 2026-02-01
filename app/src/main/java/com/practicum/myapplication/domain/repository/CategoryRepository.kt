package com.practicum.myapplication.domain.repository

import com.practicum.myapplication.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategoriesFlow(): Flow<List<Category>>
    suspend fun addCategory(name: String)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}