@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.autometazapper.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autometazapper.ui.components.ExifOptions
import com.example.autometazapper.ui.components.PhotoPicker
import com.example.autometazapper.ui.components.PhotoPreview
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Request runtime permission to read photos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 101)
            }
        } else {
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 102)
            }
        }

        setContent {
            AutoMetaZapperUI()
        }
    }
}

@Composable
fun AutoMetaZapperUI(vm: MainViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "autoMetaZapper",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // ✅ Scrollable
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ✅ Photo preview
            PhotoPreview(uri = uiState.selectedImageUri)

            // ✅ Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PhotoPicker { uri -> vm.setImageUri(uri) }

                Button(
                    onClick = {
                        try {
                            vm.cleanAndShare(context)
                            scope.launch {
                                snackbarHostState.showSnackbar("✅ Metadata cleaned and ready to share!")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            scope.launch {
                                snackbarHostState.showSnackbar("⚠️ Failed to clean image. Check Logcat for details.")
                            }
                        }
                    }
                ) {
                    Text("Clean & Share")
                }
            }

            // ✅ Auto clean toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Auto-clean:", modifier = Modifier.weight(1f))
                Switch(
                    checked = uiState.autoCleanEnabled,
                    onCheckedChange = { vm.toggleAutoClean(it) }
                )
            }

            // ✅ EXIF field options
            ExifOptions(
                selectedFields = uiState.selectedFields,
                onToggle = { field: ExifField, enabled: Boolean ->
                    vm.toggleField(field, enabled)
                }
            )
        }
    }
}

