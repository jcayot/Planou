package com.cayot.flyingmore.ui.home.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.domain.GenerateAndGetAllFlyingStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Year

class StatisticsHomeViewModel(
    private val generateAndGetAllFlyingStatisticsUseCase: GenerateAndGetAllFlyingStatisticsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsHomeUIState())
    val uiState : StateFlow<StatisticsHomeUIState> = _uiState

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