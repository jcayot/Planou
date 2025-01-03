package com.cayot.flyingmore.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTimePicker(
    selectedHour: Int?,
    selectedMinute: Int?,
    labelText: String,
    visible: Boolean,
    onTimeSelected: (Int, Int) -> Unit,
    updateVisibility: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
    ) {
    val formattedTime = if (selectedHour != null && selectedMinute != null) {
        String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
    } else {
        ""
    }
    OutlinedTextFieldButton(
        label = { Text(labelText) },
        value = formattedTime,
        onClick = { updateVisibility(true) },
        enabled = enabled,
        modifier = modifier
    )
    if (enabled) {
        val timePickerState = if (selectedHour != null && selectedMinute != null)
                rememberTimePickerState(
                    initialHour = selectedHour,
                    initialMinute = selectedMinute
                ) else rememberTimePickerState(
                initialHour = 0,
                initialMinute = 0)

        if (visible) {
            AlertDialog(
                onDismissRequest = { updateVisibility(false) },
                dismissButton = {
                    TextButton(onClick = { updateVisibility(false) }) {
                        Text("Dismiss")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        onTimeSelected(timePickerState.hour, timePickerState.minute)
                        updateVisibility(false)
                    }) {
                        Text("OK")
                    }
                },
                text = {
                    TimePicker(
                        state = timePickerState
                    )
                }
            )
        }
    }
}