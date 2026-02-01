package com.practicum.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // Хранит данные
    private val _clickCounter = MutableLiveData<Int>()

    // Экспонирует только для чтения
    val clickCounter: LiveData<Int> = _clickCounter

    fun increment() {
        _clickCounter.value = (_clickCounter.value ?: 0) + 1
    }
}
