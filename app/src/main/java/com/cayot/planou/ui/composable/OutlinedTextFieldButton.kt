package com.cayot.planou.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OutlinedTextFieldButton(
	label: @Composable (() -> Unit)?,
	value: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	trailingIcon: @Composable (() -> Unit)? = null,
	) {
	Box (
		modifier = modifier
	) {
		OutlinedTextField(
			label = label,
			value = value,
			trailingIcon = trailingIcon,
			onValueChange = {  },
			singleLine = true,
			readOnly = true,
			enabled = enabled,
			shape = shapes.large,
			modifier = modifier.fillMaxWidth()
		)
		Box(
			modifier = Modifier
				.matchParentSize()
				.clickable(onClick = onClick),
		)
	}
}