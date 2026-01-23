package com.practicum.myapplication.domain.usecase

abstract class BaseUseCase<in Input, out Output> {
    abstract suspend operator fun invoke(input: Input): Output
}

abstract class NoParamsUseCase<out Output> {
    abstract suspend operator fun invoke(): Output
}