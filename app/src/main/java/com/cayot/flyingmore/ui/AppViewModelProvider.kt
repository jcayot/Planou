package com.cayot.flyingmore.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cayot.flyingmore.FlyingMoreApplication
import com.cayot.flyingmore.domain.DeleteFlightWithNoteUseCase
import com.cayot.flyingmore.ui.flightEdit.FlightEditViewModel
import com.cayot.flyingmore.ui.flightDetails.FlightDetailsViewModel
import com.cayot.flyingmore.ui.flightList.FlightListViewModel

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
	}
}

fun CreationExtras.planouApplication() : FlyingMoreApplication =
	(this[AndroidViewModelFactory.APPLICATION_KEY] as FlyingMoreApplication)