package com.cayot.flyingmore.ui.home.statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.domain.GenerateAndGetAllFlyingStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Year

class StatisticHomeViewModel(
    private val generateAndGetAllFlyingStatisticsUseCase: GenerateAndGetAllFlyingStatisticsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticHomeUIState())
    val uiState : StateFlow<StatisticHomeUIState> = _uiState

    init {
        viewModelScope.launch {
            generateAndGetAllFlyingStatisticsUseCase.invoke(Year.now().value).collect { statistics ->
                _uiState.update {
                    it.copy(statisticsList = statistics.map { it -> it.toTemporalStatisticBrief() })
                }
            }
        }
    }
}