package com.cayot.flyingmore.ui.home.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.model.statistics.TemporalStatisticBrief
import com.cayot.flyingmore.ui.AppViewModelProvider

@Composable
fun HomeStatisticsTab(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel: StatisticsHomeViewModel = viewModel(factory = AppViewModelProvider.factory)
    val uiState by viewModel.uiState.collectAsState()

    HomeStatisticsScreenContent(
        uiState = uiState,
        onStatisticItemPressed = {},
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    )
}

@Composable
fun HomeStatisticsScreenContent(
    uiState: StatisticsHomeUIState,
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (uiState.statisticsList.isEmpty()) {
        //TODO General empty placeholder composable
    } else {

    }
}

@Composable
fun HomeStatisticsAllStatisticsList(
    uiState: StatisticsHomeUIState,
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_smadium)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_smadium)),
    )  {
        Text(
            text = stringResource(R.string.all_statistics),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_large))
                .padding(bottom = dimensionResource(R.dimen.padding_small))
        )
        LazyColumn (
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_smadium)),
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_smadium)),
        ) {

        }
    }
}

@Composable
fun StatisticItemBriefComposable(
    statisticBriefItem: TemporalStatisticBrief,
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

}