package com.cayot.planou.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun LabelledData(
    horizontalAlignment: Alignment.Horizontal,
    labelText: String,
    dataText: String,
    dataStyle: TextStyle,
    modifier: Modifier = Modifier,
    dataWeight: FontWeight? = dataStyle.fontWeight,
    dataOverflow: TextOverflow = TextOverflow.Clip,
    labelStyle: TextStyle = typography.titleSmall
) {
    Column (
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(
            text = labelText,
            style = labelStyle,
            maxLines = 1
        )
        Text(
            text = dataText,
            style = dataStyle,
            fontWeight = dataWeight,
            overflow = dataOverflow,
            maxLines = 1
        )
    }
}