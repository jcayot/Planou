package com.cayot.planou.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchTextField(
    labelText: String,
    value: String,
    items: List<T>,
    onValueChange : (String) -> Unit,
    onItemClicked : (T) -> Unit,
    onDismissed: (String, List<T>) -> Unit,
    updateVisibility: (Boolean) -> Unit,
    expanded: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = updateVisibility,
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ) {
        OutlinedTextField(
            label = { Text(labelText) },
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            enabled = enabled,
            shape = shapes.large,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = {
                onDismissed(value, items)
                updateVisibility(false) },
        ) {
            if (value.isBlank()) {

            } else if (items.isEmpty()) {

            } else {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item.toString(),
                            maxLines = 1) },
                        onClick = {
                            onItemClicked(item)
                            updateVisibility(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}
