package com.natlwd.coroutine.model

sealed class NetworkResult<out T> {
    data class Completed<T>(val data: T) : NetworkResult<T>()
    data class Failed(val throwable: Throwable) : NetworkResult<Nothing>()
    object Started : NetworkResult<Nothing>()
}