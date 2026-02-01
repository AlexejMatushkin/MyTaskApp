package com.practicum.myapplication.domain.usecase.category

import com.practicum.myapplication.domain.model.Category
import com.practicum.myapplication.domain.repository.CategoryRepository
import com.practicum.myapplication.domain.usecase.BaseUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) : BaseUseCase<Category, Unit>() {
    override suspend operator fun invoke(input: Category) {
        repository.updateCategory(input)
    }
}