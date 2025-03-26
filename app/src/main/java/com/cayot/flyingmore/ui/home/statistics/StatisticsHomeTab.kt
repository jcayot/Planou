package com.cayot.flyingmore.ui.home.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.model.statistics.TemporalStatisticBrief
import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.ui.AppViewModelProvider
import com.cayot.flyingmore.ui.composable.DataUnitField
import com.cayot.flyingmore.ui.composable.EmptyPlaceholder
import com.cayot.flyingmore.ui.composable.charts.LineGraph
import com.cayot.flyingmore.ui.composable.charts.PieChart
import com.cayot.flyingmore.ui.composable.charts.VerticalBarGraph
import java.time.Year

@Composable
fun HomeStatisticsTab(
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel: StatisticsHomeViewModel = viewModel(factory = AppViewModelProvider.factory)
    val uiState by viewModel.uiState.collectAsState()

    HomeStatisticsScreenContent(
        uiState = uiState,
        onStatisticItemPressed = onStatisticItemPressed,
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
        EmptyPlaceholder(
            painter = painterResource(R.drawable.bar_chart_off_24dp_999999_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.no_statistics_image),
            text = stringResource(R.string.no_statistics_yet),
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        )
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_smadium)),
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_smadium))
        ) {
            HomeStatisticsAllStatisticsList(
                allStatistics = uiState.statisticsList,
                onStatisticItemPressed = onStatisticItemPressed,
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
fun HomeStatisticsAllStatisticsList(
    allStatistics: List<TemporalStatisticBrief>,
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
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
        items(allStatistics) {
            StatisticItemBriefComposable(
                statisticBriefItem = it,
                onStatisticItemPressed = onStatisticItemPressed
            )
        }
    }
}

@Composable
fun StatisticItemBriefComposable(
    statisticBriefItem: TemporalStatisticBrief,
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onStatisticItemPressed(statisticBriefItem.id) },
        modifier = modifier
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .size(dimensionResource(R.dimen.list_item_height))
            .padding(dimensionResource(R.dimen.padding_small))
        ) {
            StatisticItemChart(
                chartData = statisticBriefItem.data,
                chartType = statisticBriefItem.chartType,
                modifier = Modifier.fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(vertical = dimensionResource(R.dimen.padding_small))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_smadium))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(statisticBriefItem.displayNameRes),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = statisticBriefItem.timeFrameName,
                        fontWeight = FontWeight.Light
                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                DataUnitField(
                    data = statisticBriefItem.dataText ?:
                    (statisticBriefItem.data.last().toString()),
                    unit = statisticBriefItem.unitRes?.let { stringResource(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun StatisticItemChart(
    chartData: List<Int>,
    chartType: ChartType,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        when (chartType) {
            ChartType.BAR_GRAPH -> VerticalBarGraph(renderData = chartData)
            ChartType.LINE_GRAPH -> LineGraph(renderData = chartData)
            ChartType.PIE_CHART -> PieChart(renderData = chartData)
        }
    }
}

@Preview
@Composable
fun StatisticItemBriefComposablePreview() {
    StatisticItemBriefComposable(
        statisticBriefItem = TemporalStatisticBrief(
            displayNameRes = R.string.statistic_name,
            timeFrameName = Year.of(2024).toString(),
            unitRes = null,
            data = listOf(1634, 1234, 2435, 0, 134, 9823, 12345, 450, 987, 456, 325, 943),
            chartType = ChartType.BAR_GRAPH,
            dataText = null
        ),
        onStatisticItemPressed = {}
    )
}