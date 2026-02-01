package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.local.dao.CategoryDao
import com.practicum.myapplication.data.local.entity.CategoryEntity
import com.practicum.myapplication.domain.repository.CategoryRepository
import com.practicum.myapplication.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategoriesFlow(): Flow<List<Category>> {
        return categoryDao.getAllCategoriesFlow()
            .map { entities -> entities.map { it.toCategory() } }
    }

    override suspend fun addCategory(name: String) {
        val newId = (System.currentTimeMillis() % 1000000).toInt()
        categoryDao.insertCategory(CategoryEntity(newId, name))
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }
}

fun Category.toEntity() = CategoryEntity(id, name, color)
fun CategoryEntity.toCategory() = Category(id, name, color)
