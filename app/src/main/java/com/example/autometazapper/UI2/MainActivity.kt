@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.autometazapper.ui



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autometazapper.ui.components.PhotoPicker
import com.example.autometazapper.ui.components.PhotoPreview
import com.example.autometazapper.ui.components.ExifOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoMetaZapperUI()
        }
    }
}

@Composable
fun AutoMetaZapperUI(vm: MainViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()

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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PhotoPreview(uri = uiState.selectedImageUri)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PhotoPicker { uri -> vm.setImageUri(uri) }

                Button(onClick = { /* TODO: Clean & Share */ }) {
                    Text("Clean & Share")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Auto-clean:", modifier = Modifier.weight(1f))
                Switch(
                    checked = uiState.autoCleanEnabled,
                    onCheckedChange = { vm.toggleAutoClean(it) }
                )
            }

            ExifOptions(
                selectedFields = uiState.selectedFields,
                onToggle = { field, enabled -> vm.toggleField(field, enabled) }
            )
        }
    }
}
