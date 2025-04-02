package com.cayot.flyingmore.ui.statistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.model.statistics.MapStringNumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.NumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import com.cayot.flyingmore.ui.AppViewModelProvider
import com.cayot.flyingmore.ui.PlanouTopBar
import com.cayot.flyingmore.ui.navigation.FlyingMoreScreen
import java.time.Year

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlyingStatisticScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: FlyingStatisticViewModel = viewModel(factory = AppViewModelProvider.factory)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            PlanouTopBar(
                title = stringResource(
                    if (uiState.statisticData != null)
                        uiState.getStatisticType().displayNameResource
                    else
                        FlyingMoreScreen.Statistic.title
                ),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        FlyingStatisticScreenContent(
            uiState = uiState,
            updateUiState = viewModel::updateUiState,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun FlyingStatisticScreenContent(
    uiState: FlyingStatisticUIState,
    updateUiState: (FlyingStatisticUIState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_smadium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
    ) {
        if (uiState.statisticData != null) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.getStatisticType().allowedDisplayResolutions.forEachIndexed { index, resolution ->
                    SegmentedButton(
                        label = { Text(stringResource(resolution.displayNameResource)) },
                        selected = uiState.displayResolution == resolution,
                        onClick = { updateUiState(uiState.copy(displayResolution = resolution)) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = uiState.getStatisticType().allowedDisplayResolutions.size
                        )
                    )
                }
            }
            when (uiState.statisticData) {
                is StatisticData.MapStringNumber ->
                    MapStringNumberStatisticDisplay(
                        statisticData = uiState.statisticData.mapStringNumberTemporalStatistic,
                        displayResolution = uiState.displayResolution,
                    )
                is StatisticData.Number ->
                    NumberStatisticDisplay(
                        statisticData = uiState.statisticData.numberYearTemporalStatistic,
                        displayResolution = uiState.displayResolution,
                    )
            }
        }
    }
}

@Composable
fun NumberStatisticDisplay(
    statisticData: NumberDailyTemporalStatistic,
    displayResolution: TimeFrame
) {

}

@Composable
fun MapStringNumberStatisticDisplay(
    statisticData: MapStringNumberDailyTemporalStatistic,
    displayResolution: TimeFrame
) {

}

@Preview(showBackground = true)
@Composable
fun FlyingStatisticScreenContentPreview() {
    FlyingStatisticScreenContent(
        uiState = FlyingStatisticUIState(
            StatisticData.Number(NumberDailyTemporalStatistic(
                timeFrameStart = Year.of(2024).atDay(1).atStartOfDay().toLocalDate(),
                timeFrameEnd = Year.of(2025).atDay(1).atStartOfDay().toLocalDate(),
                data = List(366, { 1 }),
                statisticType = FlyingStatistic.NUMBER_OF_FLIGHT
            )),
            displayResolution = TimeFrame.MONTH,
        ),
        updateUiState = {}
    )
}