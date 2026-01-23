package com.practicum.myapplication.domain.usecase.category

import com.practicum.myapplication.domain.repository.CategoryRepository
import com.practicum.myapplication.domain.usecase.BaseUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) : BaseUseCase<String, Unit>() {
    override suspend operator fun invoke(input: String) {
        repository.addCategory(input)
    }
}