package com.practicum.myapplication.domain.usecase

abstract class FlowUseCase<out T> {
    abstract fun execute(): kotlinx.coroutines.flow.Flow<T>
}