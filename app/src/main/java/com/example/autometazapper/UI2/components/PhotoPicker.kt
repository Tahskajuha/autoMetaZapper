package com.example.autometazapper.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PhotoPicker(onImagePicked: (Uri?) -> Unit) {
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = onImagePicked
    )

    Button(onClick = { pickImage.launch("image/*") }) {
        Text("Pick Photo")
    }
}