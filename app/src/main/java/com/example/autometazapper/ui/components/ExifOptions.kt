package com.example.autometazapper.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.autometazapper.ui.ExifField

@Composable
fun ExifOptions(
    selectedFields: Set<ExifField>,
    onToggle: (ExifField, Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Select EXIF fields to remove:", style = MaterialTheme.typography.titleMedium)
        ExifField.values().forEach { field ->
            val checked = selectedFields.contains(field)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onToggle(field, !checked) }
                    .padding(8.dp)
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onToggle(field, it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = field.displayName)

            }
        }
    }
}