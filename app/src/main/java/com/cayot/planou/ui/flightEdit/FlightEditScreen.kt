package com.cayot.planou.ui.flightEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.planou.R
import com.cayot.planou.data.TravelClass
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.ui.AppViewModelProvider
import com.cayot.planou.ui.PlanouTopBar
import com.cayot.planou.ui.composable.OutlinedDatePicker
import com.cayot.planou.ui.composable.SearchTextField
import com.cayot.planou.ui.composable.OutlinedTimePicker
import com.cayot.planou.ui.navigation.PlanouScreen
import com.cayot.planou.utils.SelectableDateAll
import com.cayot.planou.utils.SelectableDatesTo
import com.cayot.planou.utils.updateInstantDate
import com.cayot.planou.utils.updateInstantHourMinute
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightEditScreen(
	navigateBack: () -> Unit,
	onNavigateUp: () -> Unit,
	canNavigateBack: Boolean = true
) {
	val	viewModel: FlightEditViewModel = viewModel(factory = AppViewModelProvider.factory)
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.navigateBack.collect{
			navigateBack()
		}
	}

	Scaffold (
		topBar = {
			PlanouTopBar(
				title = stringResource(PlanouScreen.Edit.title),
				canNavigateBack = canNavigateBack,
				navigateUp = onNavigateUp
			)
		}
	) { innerPadding ->
		FlightEditScreenContent(
			uiState = uiState,
			updateFlightDetails = viewModel::updateFlightForm,
			searchDepartureAirport = viewModel::searchDepartureAirport,
			departureAirportSelected = viewModel::departureAirportSelected,
			searchArrivalAirport = viewModel::searchArrivalAirport,
			arrivalAirportSelected = viewModel::arrivalAirportSelected,
			updateFormElementVisibility = viewModel::updateFormElementVisibility,
			onContinueClicked = viewModel::saveFlight,
			modifier = Modifier
				.padding(
					start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
					end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
					top = innerPadding.calculateTopPadding()
				)
				.fillMaxWidth()
		)
	}
}

@Composable
fun FlightEditScreenContent(
	uiState: FlightEditUIState,
	updateFlightDetails: (FlightForm) -> Unit = {},
	searchDepartureAirport: (String) -> Unit = {},
	departureAirportSelected: (Airport) -> Unit = {},
	searchArrivalAirport: (String) -> Unit = {},
	arrivalAirportSelected: (Airport) -> Unit = {},
	updateFormElementVisibility: (FormElementVisibility) -> Unit = {},
	onContinueClicked: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	Column (
		verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
	) {
		FlightEditForm(
			uiState = uiState,
			updateFlightDetails = updateFlightDetails,
			searchDepartureAirport = searchDepartureAirport,
			departureAirportSelected = departureAirportSelected,
			searchArrivalAirport = searchArrivalAirport,
			arrivalAirportSelected = arrivalAirportSelected,
			updateFormElementVisibility = updateFormElementVisibility,
			modifier = Modifier.fillMaxWidth()
		)
		Button(
			onClick = { onContinueClicked() },
			enabled = uiState.isEntryValid && uiState.formEnabled,
		) {
			Text(
				text = stringResource(R.string.save_flight),
				fontSize = 16.sp,
				modifier = Modifier.padding(dimensionResource(R.dimen.padding_mini))
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightEditForm(
	uiState: FlightEditUIState,
	updateFlightDetails: (FlightForm) -> Unit,
	searchDepartureAirport: (String) -> Unit,
	departureAirportSelected: (Airport) -> Unit,
	searchArrivalAirport: (String) -> Unit,
	arrivalAirportSelected: (Airport) -> Unit,
	updateFormElementVisibility: (FormElementVisibility) -> Unit,
	modifier: Modifier = Modifier
) {
	Card(
		elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
		modifier = modifier
	) {
		Column (
			modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
			verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
		) {
			Text(
				text = stringResource(R.string.flight_informations),
				style = typography.titleMedium
			)
			Row (
				modifier = Modifier.fillMaxWidth()
					.height(56.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				SearchTextField(
					labelText = stringResource(R.string.flight_origin),
					value = uiState.flightForm.originAirportString,
					items = uiState.flightForm.foundOriginAirportsList,
					onValueChange = searchDepartureAirport,
					onItemClicked = departureAirportSelected,
					updateVisibility = {
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							originAirportDropdownVisible = it
						))
					},
					expanded = uiState.formElementVisibility.originAirportDropdownVisible,
					enabled = uiState.formEnabled,
					modifier = Modifier.weight(1f)
				)
				Spacer(modifier = Modifier.width(8.dp))
				SearchTextField(
					labelText = stringResource(R.string.flight_destination),
					value = uiState.flightForm.destinationAirportString,
					items = uiState.flightForm.foundDestinationAirportList,
					onValueChange = searchArrivalAirport,
					onItemClicked = arrivalAirportSelected,
					updateVisibility = {
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							destinationAirportDropdownVisible = it
						))
					},
					expanded = uiState.formElementVisibility.destinationAirportDropdownVisible,
					enabled = uiState.formEnabled,
					modifier = Modifier.weight(1f)
				)
			}
			OutlinedTextField(
				label = { Text(stringResource(R.string.flight_airline)) },
				value = uiState.flightForm.airline,
				onValueChange = { updateFlightDetails(uiState.flightForm.copy(airline = it)) },
				singleLine = true,
				enabled = uiState.formEnabled,
				shape = shapes.large,
				modifier = modifier
			)
			Row (
				modifier = modifier,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				TravelClassDropdown(
					currentTravelClass = uiState.flightForm.travelClass,
					expanded = uiState.formElementVisibility.travelClassDropdownVisible,
					onTravelClassSelected = { updateFlightDetails(uiState.flightForm.copy(travelClass = it)) },
					updateVisibility = { visibility ->
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							travelClassDropdownVisible = visibility
						))
					},
					enabled = uiState.formEnabled,
					modifier = Modifier.weight(1f)
				)
				Spacer(modifier = Modifier.width(8.dp))
				OutlinedTextField(
					label = { Text(stringResource(R.string.flight_number)) },
					value = uiState.flightForm.flightNumber,
					onValueChange = { updateFlightDetails(uiState.flightForm.copy(flightNumber = it)) },
					singleLine = true,
					enabled = uiState.formEnabled,
					shape = shapes.large,
					modifier = Modifier.weight(1f)
				)

			}
			OutlinedTextField(
				label = { Text(stringResource(R.string.plane_model)) },
				value = uiState.flightForm.planeModel,
				onValueChange = { updateFlightDetails(uiState.flightForm.copy(planeModel = it)) },
				singleLine = true,
				enabled = uiState.formEnabled,
				shape = shapes.large,
				modifier = modifier
			)
			Row (
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
				modifier = modifier
			) {
				OutlinedDatePicker(
					selectedInstant = uiState.flightForm.departureTime,
					visible = uiState.formElementVisibility.departureDayModalVisible,
					selectableDates = SelectableDatesTo(Instant.now()),
					labelText = stringResource(R.string.departure_day),
					onDateSelected = { date ->
						val newDepartureTime = updateInstantDate(uiState.flightForm.departureTime, Instant.ofEpochMilli(date))
						updateFlightDetails(uiState.flightForm.copy(departureTime = newDepartureTime))
					},
					updateVisibility = { visibility ->
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							departureDayModalVisible = visibility
						))
					},
					enabled = uiState.formEnabled,
					modifier = Modifier.weight(3f)
				)
				OutlinedTimePicker(
					selectedInstant = uiState.flightForm.departureTime,
					visible = uiState.formElementVisibility.departureTimeModalVisible,
					labelText = stringResource(R.string.departure_time),
					onTimeSelected = { hour, minute ->
						val newDepartureTime = updateInstantHourMinute(uiState.flightForm.departureTime, hour, minute)
						updateFlightDetails(uiState.flightForm.copy(departureTime = newDepartureTime))
					},
					updateVisibility = { visibility ->
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							departureTimeModalVisible = visibility
						))
					},
					enabled = uiState.formEnabled,
					modifier = Modifier.weight(2f)
				)
			}
			Row (
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
				modifier = modifier
			) {
				OutlinedDatePicker(
					selectedInstant = uiState.flightForm.arrivalTime,
					visible = uiState.formElementVisibility.arrivalDayModalVisible,
					selectableDates = SelectableDateAll(),
					labelText = stringResource(R.string.arrival_day),
					onDateSelected = { date ->
						val newArrivalTime = updateInstantDate(uiState.flightForm.arrivalTime, Instant.ofEpochMilli(date))
						updateFlightDetails(uiState.flightForm.copy(arrivalTime = newArrivalTime))
					},
					updateVisibility = { visibility ->
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							arrivalDayModalVisible = visibility
						))
					},
					enabled = uiState.formEnabled,
					modifier = Modifier.weight(3f)
				)
				OutlinedTimePicker(
					selectedInstant = uiState.flightForm.arrivalTime,
					visible = uiState.formElementVisibility.arrivalTimeModalVisible,
					labelText = stringResource(R.string.arrival_time),
					onTimeSelected = { hour, minute ->
						val newArrivalTime = updateInstantHourMinute(uiState.flightForm.arrivalTime, hour, minute)
						updateFlightDetails(uiState.flightForm.copy(arrivalTime = newArrivalTime))
					},
					updateVisibility = { visibility ->
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							arrivalTimeModalVisible = visibility
						))
					},
					enabled = uiState.formEnabled,
					modifier = Modifier.weight(2f)
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun	TravelClassDropdown(
	currentTravelClass: TravelClass,
	expanded: Boolean,
	onTravelClassSelected: (TravelClass) -> Unit,
	updateVisibility: (Boolean) -> Unit,
	enabled: Boolean,
	modifier: Modifier = Modifier
) {

	ExposedDropdownMenuBox(
		expanded = expanded && enabled,
		onExpandedChange = { updateVisibility(it) },
		modifier = modifier.wrapContentSize(Alignment.TopStart)
	) {
		OutlinedTextField(
			label = { Text(stringResource(R.string.travel_class)) },
			value = currentTravelClass.name,
			onValueChange = {  },
			modifier = modifier
				.menuAnchor()
				.fillMaxWidth(),
			singleLine = true,
			readOnly = true,
			enabled = enabled,
			shape = shapes.large,
			trailingIcon = {
				ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
			},
			colors = ExposedDropdownMenuDefaults.textFieldColors()
		)
		DropdownMenu(
			expanded = expanded && enabled,
			onDismissRequest = { updateVisibility(false) },
			modifier = Modifier.exposedDropdownSize()

		) {
			TravelClass.entries.forEach { travelClass ->
				DropdownMenuItem(
					text = { Text(text = travelClass.name, fontSize = 14.sp) },
					onClick = {
						onTravelClassSelected(travelClass)
						updateVisibility(false)
					},
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun FlightAddScreenPreview() {
	FlightEditScreenContent(
		uiState = FlightEditUIState(),
    )
}