package com.cayot.planou.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cayot.planou.PlanouApplication
import com.cayot.planou.ui.flightAdd.FlightAddViewModel
import com.cayot.planou.ui.flightDetails.FlightDetailsViewModel
import com.cayot.planou.ui.flightList.FlightListViewModel

object AppViewModelProvider {
	val factory = viewModelFactory {
		initializer {
			FlightListViewModel(
				flightsRepository = planouApplication().container.flightsRepository,
				airportsRepository = planouApplication().container.airportsRepository)
		}

		initializer {
			FlightAddViewModel(
				flightsRepository = planouApplication().container.flightsRepository,
				airportsRepository = planouApplication().container.airportsRepository
			)
		}

		initializer {
			FlightDetailsViewModel(
				savedStateHandle = this.createSavedStateHandle(),
				flightsRepository = planouApplication().container.flightsRepository,
				airportsRepository = planouApplication().container.airportsRepository,
				flightNotesRepository = planouApplication().container.flightNotesRepository,
				imageRepository = planouApplication().container.imageRepository
			)
		}
	}
}

fun CreationExtras.planouApplication() : PlanouApplication =
	(this[AndroidViewModelFactory.APPLICATION_KEY] as PlanouApplication)