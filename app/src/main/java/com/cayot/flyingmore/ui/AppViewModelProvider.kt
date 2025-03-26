package com.cayot.flyingmore.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cayot.flyingmore.FlyingMoreApplication
import com.cayot.flyingmore.domain.CalendarFromDayDifferenceHourMinuteUseCase
import com.cayot.flyingmore.domain.ConvertLocalTimeToCalendarUseCase
import com.cayot.flyingmore.domain.DeleteFlightWithNoteUseCase
import com.cayot.flyingmore.domain.GenerateAndGetAllFlyingStatisticsUseCase
import com.cayot.flyingmore.ui.flight.edit.FlightEditViewModel
import com.cayot.flyingmore.ui.flight.details.FlightDetailsViewModel
import com.cayot.flyingmore.ui.home.list.FlightListViewModel
import com.cayot.flyingmore.ui.home.statistics.StatisticsHomeViewModel

object AppViewModelProvider {
	val factory = viewModelFactory {
		initializer {
			FlightListViewModel(flightsRepository = planouApplication().container.flightsRepository)
		}

		initializer {
			FlightEditViewModel(
				flightsRepository = planouApplication().container.flightsRepository,
				airportsRepository = planouApplication().container.airportsRepository,
				deleteFlightWithNoteUseCase = DeleteFlightWithNoteUseCase(
					planouApplication().container.flightsRepository,
					planouApplication().container.flightNotesRepository
				),
				convertLocalTimeToCalendarUseCase = ConvertLocalTimeToCalendarUseCase(),
				calendarFromDifference = CalendarFromDayDifferenceHourMinuteUseCase(),
				savedStateHandle = this.createSavedStateHandle()
			)
		}

		initializer {
			FlightDetailsViewModel(
				flightsRepository = planouApplication().container.flightsRepository,
				flightNotesRepository = planouApplication().container.flightNotesRepository,
				imageRepository = planouApplication().container.imageRepository,
				savedStateHandle = this.createSavedStateHandle()
			)
		}

		initializer {
			StatisticsHomeViewModel(
				generateAndGetAllFlyingStatisticsUseCase = GenerateAndGetAllFlyingStatisticsUseCase(
					flyingStatisticsRepository = planouApplication().container.flyingStatisticsRepository,
					generateFlyingStatisticRepository = planouApplication().container.generateFlyingStatisticRepository
				)
			)
		}
	}
}

fun CreationExtras.planouApplication() : FlyingMoreApplication =
	(this[AndroidViewModelFactory.APPLICATION_KEY] as FlyingMoreApplication)