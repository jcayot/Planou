package com.cayot.planou.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.cayot.planou.R

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
    modifier: Modifier = Modifier,
    shape: Shape = shapes.large,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = {
            if (it)
                onValueChange(value)
            updateVisibility(it) },
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ) {
        OutlinedTextField(
            label = { Text(labelText) },
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            enabled = enabled,
            shape = shape,
            colors = colors,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled)
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = {
                onDismissed(value, items)
                updateVisibility(false) },
            modifier = Modifier.exposedDropdownSize()
        ) {
            if (value.isBlank()) {
                Text(
                    text = stringResource(R.string.type_airport_code),
                    maxLines = 1,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (items.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_airport_matching),
                    maxLines = 1,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item.toString(),
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp,
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
