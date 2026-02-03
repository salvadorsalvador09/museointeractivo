package com.kmobile.museointeractivo.ui.common

sealed interface DetailState<out T>{
    data object Loading : DetailState<Nothing>
    data class Success<T>(val data: T) : DetailState<T>
    data class Error(val message: String) : DetailState<Nothing>
    data object Empty : DetailState<Nothing>

}