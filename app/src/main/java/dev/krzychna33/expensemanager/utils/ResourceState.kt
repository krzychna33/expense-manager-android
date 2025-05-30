package dev.krzychna33.expensemanager.utils

sealed class ResourceState<T> {
    class Loading<T>: ResourceState<T>()
    data class Success<T>(val data: T): ResourceState<T>()
    data class Error<T>(val error: String): ResourceState<T>()
}