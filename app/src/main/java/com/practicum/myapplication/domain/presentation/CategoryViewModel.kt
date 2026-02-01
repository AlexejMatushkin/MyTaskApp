package com.practicum.myapplication.domain.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Category
import com.practicum.myapplication.domain.repository.CategoryRepository
import com.practicum.myapplication.domain.usecase.category.AddCategoryUseCase
import com.practicum.myapplication.domain.usecase.category.DeleteCategoryUseCase
import com.practicum.myapplication.domain.usecase.category.GetCategoriesUseCase
import com.practicum.myapplication.domain.usecase.category.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    val categories = getCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCategory(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                addCategoryUseCase(name.trim())
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            updateCategoryUseCase(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            deleteCategoryUseCase(category)
        }
    }
}
