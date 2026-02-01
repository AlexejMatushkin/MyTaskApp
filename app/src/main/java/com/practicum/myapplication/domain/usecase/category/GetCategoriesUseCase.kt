package com.practicum.myapplication.domain.usecase.category

import com.practicum.myapplication.domain.model.Category
import com.practicum.myapplication.domain.repository.CategoryRepository
import com.practicum.myapplication.domain.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getAllCategoriesFlow()
    }
}
