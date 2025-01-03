package com.cayot.flyingmore.ui.composable

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDatePicker(
    selectedDate: Long?,
    selectableDates: SelectableDates,
    labelText: String,
    visible: Boolean,
    onDateSelected: (Long) -> Unit,
    updateVisibility: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    ) {
    val formattedDate = if (selectedDate != null)
        SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(selectedDate)
    else
        ""

    OutlinedTextFieldButton(
        label = { Text(labelText) },
        value = formattedDate,
        onClick = { updateVisibility(true) },
        enabled = enabled,
        modifier = modifier,
    )
    if (enabled) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate,
            selectableDates = selectableDates
        )
        if (visible) {
            DatePickerDialog(
                onDismissRequest = { updateVisibility(false) },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                        updateVisibility(false)
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        updateVisibility(false) }
                    ) {
                        Text("Cancel")
                    }
                },
            ) {
                DatePicker(
                    state = datePickerState,
                )
            }
        }
    }
}