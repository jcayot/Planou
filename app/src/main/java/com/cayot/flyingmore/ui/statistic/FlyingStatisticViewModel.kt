package com.cayot.flyingmore.ui.statistic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.data.model.statistics.MapStringNumberDailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.NumberDailyTemporalStatistic
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import com.cayot.flyingmore.ui.navigation.FlyingMoreScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlyingStatisticViewModel(
    private val flyingStatisticsRepository: FlyingStatisticsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val flyingStatisticId: Int = checkNotNull(savedStateHandle[FlyingMoreScreen.Statistic.argName])

    private val _uiState = MutableStateFlow(FlyingStatisticUIState())
    val uiState: StateFlow<FlyingStatisticUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            flyingStatisticsRepository.getFlyingStatistic(flyingStatisticId).collect { flyingStatistic ->
                _uiState.value = _uiState.value.copy(
                    statisticData = if (flyingStatistic is NumberDailyTemporalStatistic)
                        StatisticData.Number(flyingStatistic)
                    else if (flyingStatistic is MapStringNumberDailyTemporalStatistic)
                        StatisticData.MapStringNumber(flyingStatistic)
                    else
                        throw InternalError("Unknown statistic type"),
                    displayResolution = flyingStatistic.statisticType.defaultDisplayResolution,
                )
            }
        }
    }

    fun updateUiState(uiState: FlyingStatisticUIState) {
        _uiState.value = uiState
    }
}