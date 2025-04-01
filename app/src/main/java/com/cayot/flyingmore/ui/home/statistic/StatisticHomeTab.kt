package com.cayot.flyingmore.ui.home.statistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.model.statistics.TemporalStatisticBrief
import com.cayot.flyingmore.data.model.statistics.Trend
import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.ui.AppViewModelProvider
import com.cayot.flyingmore.ui.composable.DataUnitField
import com.cayot.flyingmore.ui.composable.EmptyPlaceholder
import com.cayot.flyingmore.ui.composable.charts.LineGraph
import com.cayot.flyingmore.ui.composable.charts.PieChart
import com.cayot.flyingmore.ui.composable.charts.VerticalBarGraph
import com.cayot.flyingmore.ui.composable.ListComposable
import java.time.Year

@Composable
fun HomeStatisticTab(
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: StatisticHomeViewModel = viewModel(factory = AppViewModelProvider.factory)
    val uiState by viewModel.uiState.collectAsState()

    HomeStatisticScreenContent(
        uiState = uiState,
        onStatisticItemPressed = onStatisticItemPressed,
        modifier = modifier,
    )
}

@Composable
fun HomeStatisticScreenContent(
    uiState: StatisticHomeUIState,
    onStatisticItemPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListComposable(
        uiState.statisticsList,
        onItemPressed = onStatisticItemPressed,
        itemComposable = { statisticBriefItem, onStatisticItemPressed ->
            StatisticItemBriefComposable(
                statisticBriefItem = statisticBriefItem,
                onStatisticItemPressed = onStatisticItemPressed
            )
        },
        modifier = modifier,
        emptyPlaceholder = {
            EmptyPlaceholder(
                painter = painterResource(R.drawable.bar_chart_off_24dp_999999_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.no_statistics_image),
                text = stringResource(R.string.no_statistics_yet),
                modifier = modifier
            )
        },
        title = {StatisticListTitle()},
        listItemVerticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_smadium))
    )
}

@Composable
fun StatisticListTitle() {
    Text(
        text = stringResource(R.string.all_statistics),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_large))
            .padding(vertical = dimensionResource(R.dimen.padding_small))
    )
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
            .size(dimensionResource(R.dimen.statistic_list_item_heigth))
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
                        text = statisticBriefItem.timeFrameString,
                        fontWeight = FontWeight.Light
                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.padding_small))
                        .padding(start = dimensionResource(R.dimen.padding_small))
                ) {
                    DataUnitField(
                        data = statisticBriefItem.dataText,
                        unit = statisticBriefItem.unitRes?.let { stringResource(it) },
                    )
                    if (statisticBriefItem.trend != null) {
                        Badge(
                            containerColor = colorResource(statisticBriefItem.trend.colorRes),
                            contentColor = Color.White,
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                                .size(dimensionResource(R.dimen.badge_size))
                        ) {
                            Icon(
                                imageVector = statisticBriefItem.trend.icon,
                                contentDescription = statisticBriefItem.trend.icon.name
                            )
                        }
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun HomeStatisticScreenContentPreview() {
    HomeStatisticScreenContent(
        uiState = StatisticHomeUIState(listOf(
            TemporalStatisticBrief(
                displayNameRes = R.string.statistic_name,
                timeFrameString = Year.of(2024).toString(),
                unitRes = null,
                data = listOf(1634, 1234, 2435, 0, 134, 9823, 12345, 450, 987, 456, 325, 943),
                chartType = ChartType.BAR_GRAPH,
                dataText = "1634",
                trend = Trend.INCREASING
            ),
            TemporalStatisticBrief(
                displayNameRes = R.string.statistic_name,
                timeFrameString = Year.of(2024).toString(),
                unitRes = null,
                data = listOf(1634, 1234, 2435, 0, 134, 9823, 12345, 450, 987, 456, 325, 943),
                chartType = ChartType.BAR_GRAPH,
                dataText = "1634",
                trend = Trend.INCREASING
            )
        )),
        onStatisticItemPressed = {}
    )
}

@Preview
@Composable
fun StatisticItemBriefComposablePreview() {
    StatisticItemBriefComposable(
        statisticBriefItem = TemporalStatisticBrief(
            displayNameRes = R.string.statistic_name,
            timeFrameString = Year.of(2024).toString(),
            unitRes = null,
            data = listOf(1634, 1234, 2435, 0, 134, 9823, 12345, 450, 987, 456, 325, 943),
            chartType = ChartType.BAR_GRAPH,
            dataText = "1634",
            trend = Trend.INCREASING
        ),
        onStatisticItemPressed = {}
    )
}