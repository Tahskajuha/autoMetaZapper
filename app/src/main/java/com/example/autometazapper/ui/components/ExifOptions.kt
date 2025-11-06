package com.example.autometazapper.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autometazapper.ui.ExifField

@Composable
fun ExifOptions(
    selectedFields: Set<ExifField>,
    onToggle: (ExifField, Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Select EXIF fields to remove:",
            style = MaterialTheme.typography.titleMedium
        )

        ExifField.values().forEach { field ->
            val isChecked = selectedFields.contains(field)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(field, !isChecked) },
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onToggle(field, it) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = field.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
