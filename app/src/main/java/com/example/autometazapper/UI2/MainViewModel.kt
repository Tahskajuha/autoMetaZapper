package com.example.autometazapper.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

enum class ExifField(val displayName: String) {
    LOCATION("Location"),
    DATE_TIME("Date & Time"),
    CAMERA_MODEL("Camera Model"),
    ORIENTATION("Orientation")
}

data class UiState(
    val selectedImageUri: Uri? = null,
    val autoCleanEnabled: Boolean = false,
    val selectedFields: Set<ExifField> = emptySet()
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun setImageUri(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    fun toggleAutoClean(enabled: Boolean) {
        _uiState.update { it.copy(autoCleanEnabled = enabled) }
    }

    fun toggleField(field: ExifField, enabled: Boolean) {
        _uiState.update {
            val newFields = it.selectedFields.toMutableSet()
            if (enabled) newFields.add(field) else newFields.remove(field)
            it.copy(selectedFields = newFields)
        }
    }
}
