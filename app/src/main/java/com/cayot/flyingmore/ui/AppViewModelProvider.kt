package com.cayot.flyingmore.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cayot.flyingmore.FlyingMoreApplication
import com.cayot.flyingmore.domain.CalendarFromDayDifferenceHourMinuteUseCase
import com.cayot.flyingmore.domain.ConvertLocalTimeToUtcCalendarUseCase
import com.cayot.flyingmore.domain.DeleteFlightWithNoteUseCase
import com.cayot.flyingmore.domain.GenerateAndGetAllFlyingStatisticsUseCase
import com.cayot.flyingmore.ui.flight.edit.FlightEditViewModel
import com.cayot.flyingmore.ui.flight.details.FlightDetailsViewModel
import com.cayot.flyingmore.ui.home.flight.FlightListViewModel
import com.cayot.flyingmore.ui.home.statistic.StatisticHomeViewModel
import com.cayot.flyingmore.ui.statistic.FlyingStatisticViewModel

object AppViewModelProvider {
	val factory = viewModelFactory {
		initializer {
			FlightListViewModel(flightRepository = planouApplication().container.flightRepository)
		}

		initializer {
			FlightEditViewModel(
				flightRepository = planouApplication().container.flightRepository,
				airportsRepository = planouApplication().container.airportsRepository,
				deleteFlightWithNoteUseCase = DeleteFlightWithNoteUseCase(
					planouApplication().container.flightRepository,
					planouApplication().container.flightNotesRepository
				),
				convertLocalTimeToUtcCalendarUseCase = ConvertLocalTimeToUtcCalendarUseCase(),
				calendarFromDifference = CalendarFromDayDifferenceHourMinuteUseCase(),
				savedStateHandle = this.createSavedStateHandle()
			)
		}

		initializer {
			FlightDetailsViewModel(
				flightRepository = planouApplication().container.flightRepository,
				flightNotesRepository = planouApplication().container.flightNotesRepository,
				imageRepository = planouApplication().container.imageRepository,
				savedStateHandle = this.createSavedStateHandle()
			)
		}

		initializer {
			StatisticHomeViewModel(
				generateAndGetAllFlyingStatisticsUseCase = GenerateAndGetAllFlyingStatisticsUseCase(
					flyingStatisticsRepository = planouApplication().container.flyingStatisticsRepository,
					generateFlyingStatisticRepository = planouApplication().container.generateFlyingStatisticRepository
				)
			)
		}

		initializer {
			FlyingStatisticViewModel(
				flyingStatisticsRepository = planouApplication().container.flyingStatisticsRepository,
				savedStateHandle = this.createSavedStateHandle()
			)
		}
	}
}

fun CreationExtras.planouApplication() : FlyingMoreApplication =
	(this[AndroidViewModelFactory.APPLICATION_KEY] as FlyingMoreApplication)