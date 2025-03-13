package com.cayot.flyingmore.ui.home.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cayot.flyingmore.data.repository.FlyingStatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsHomeViewModel(
    private val flyingStatisticsRepository: FlyingStatisticsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsHomeUIState())
    val uiState : StateFlow<StatisticsHomeUIState> = _uiState

    init {
        viewModelScope.launch {
            flyingStatisticsRepository.getAllFlyingStatistics<Object>().collect { statistics ->
                _uiState.update {
                    it.copy(statisticsList = statistics.map { it -> it.toTemporalStatisticBrief() })
                }
            }
        }
    }
}