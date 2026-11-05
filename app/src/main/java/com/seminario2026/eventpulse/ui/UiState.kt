package com.seminario2026.eventpulse.ui

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Empty(val message: String = "Sin resultados disponibles") : UiState<Nothing>
    data class Error(val message: String, val onRetry: (() -> Unit)? = null) : UiState<Nothing>
}
