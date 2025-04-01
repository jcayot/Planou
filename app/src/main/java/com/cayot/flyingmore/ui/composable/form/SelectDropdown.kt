package com.cayot.flyingmore.ui.composable.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectDropdown(
	labelText: String,
	items: List<T>,
	selected: T,
	onItemSelected: (T) -> Unit,
	updateVisibility: (Boolean) -> Unit,
	expanded: Boolean,
	enabled: Boolean,
	modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { updateVisibility(it) },
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ) {
        OutlinedTextField(
            label = { Text(labelText) },
            value = selected.toString(),
            onValueChange = { },
            modifier = modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled)
                .fillMaxWidth(),
            singleLine = true,
            readOnly = true,
            enabled = enabled,
            shape = MaterialTheme.shapes.large,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { updateVisibility(false) },
            modifier = Modifier.exposedDropdownSize()

        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.toString(),
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        updateVisibility(false)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}