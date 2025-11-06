package com.example.autometazapper.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.File

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

    /**
     * Cleans the selected image's EXIF metadata and shares it using FileProvider.
     */
    fun cleanAndShare(context: Context) {
        val currentUri = _uiState.value.selectedImageUri ?: return

        // Perform cleaning
        val cleanedFile: File? = ExifCleaner.cleanImage(context, currentUri, _uiState.value.selectedFields)
        if (cleanedFile == null) {
            Log.e("MainViewModel", "Failed to clean image.")
            return
        }

        try {
            // Use FileProvider for safe URI sharing
            val authority = "${context.packageName}.fileprovider"
            val contentUri = FileProvider.getUriForFile(context, authority, cleanedFile)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Use chooser + NEW_TASK for ViewModel context
            val chooser = Intent.createChooser(shareIntent, "Share cleaned photo").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(chooser)

        } catch (e: Exception) {
            Log.e("MainViewModel", "Error sharing cleaned image: ${e.message}", e)
        }
    }
}
