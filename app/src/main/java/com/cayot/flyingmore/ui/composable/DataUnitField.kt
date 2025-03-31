package com.cayot.flyingmore.ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import com.cayot.flyingmore.R

@Composable
fun DataUnitField(
    data: String,
    unit: String?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = data,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = SemiBold
        )
        if (unit != null) {
            Text(
                text = unit,
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_mini)),
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataUnitFieldPreview() {
    DataUnitField(
        data = "12",
        unit = "km"
    )
}