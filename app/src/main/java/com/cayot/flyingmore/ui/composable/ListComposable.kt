package com.cayot.flyingmore.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.cayot.flyingmore.R

@Composable
fun <T> ListComposable(
    list: List<T>,
    onItemPressed: (Int) -> Unit,
    itemComposable: @Composable (T, (Int) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    emptyPlaceholder: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    listItemVerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(dimensionResource(R.dimen.padding_smadium)),
) {
    if (list.isEmpty()) {
        emptyPlaceholder()
    } else {
        ListComposableList(
            list = list,
            onItemPressed = onItemPressed,
            itemComposable = itemComposable,
            modifier = modifier,
            title = title,
            listItemVerticalArrangement = listItemVerticalArrangement
        )
    }
}

@Composable
fun <T> ListComposableList(
    list: List<T>,
    onItemPressed: (Int) -> Unit,
    itemComposable: @Composable (T, (Int) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    listItemVerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(dimensionResource(R.dimen.padding_smadium)),
) {
    Column(
        modifier = modifier,
    ) {
        title()
        LazyColumn(
            verticalArrangement = listItemVerticalArrangement,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_smadium)),
        ) {
            items(list) {
                itemComposable(
                    it,
                    onItemPressed
                )
            }
        }
    }
}