package com.francescoalessi.sagai.api

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val exception: Throwable) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
}