package com.cayot.planou.ui.flightAdd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.cayot.planou.ui.AppViewModelProvider
import com.cayot.planou.ui.PlanouTopBar
import com.cayot.planou.ui.composable.OutlinedTextFieldButton
import com.cayot.planou.ui.navigation.PlanouScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightAddScreen(
	navigateBack: () -> Unit,
	onNavigateUp: () -> Unit,
	canNavigateBack: Boolean = true
) {
	val	viewModel: FlightAddViewModel = viewModel(factory = AppViewModelProvider.factory)
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.navigateBack.collect{
			navigateBack()
		}
	}

	Scaffold (
		topBar = {
			PlanouTopBar(
				title = stringResource(PlanouScreen.Add.title),
				canNavigateBack = canNavigateBack,
				navigateUp = onNavigateUp
			)
		}
	) { innerPadding ->
		FlightAddScreenContent(
			uiState = uiState,
			updateFlightDetails = viewModel::updateFlightDetails,
			updateTravelClassVisibility = viewModel::updateTravelClassDropdownVisibility,
			updateDateVisibility = viewModel::updateDepartureDayModalVisibility,
			onContinueClicked = viewModel::saveFlight,
			onBackClicked = navigateBack,
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
fun FlightAddScreenContent(
	uiState: FlightAddUIState,
	updateFlightDetails: (FlightForm) -> Unit,
	updateTravelClassVisibility: (Boolean) -> Unit,
	updateDateVisibility: (Boolean) -> Unit,
	onContinueClicked: () -> Unit,
	onBackClicked: () -> Unit,
	modifier: Modifier = Modifier
) {
	Column (
		verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
		modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
	) {
		FlightAddForm(
			uiState = uiState,
			updateFlightDetails = updateFlightDetails,
			updateTravelClassVisibility = updateTravelClassVisibility,
			updateDateVisibility = updateDateVisibility,
			modifier = Modifier.fillMaxWidth()
		)
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(dimensionResource(id = R.dimen.padding_medium)),
			verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
			horizontalAlignment = Alignment.CenterHorizontally
		) {

			Button(
				onClick = { onContinueClicked() },
				enabled = uiState.isEntryValid && uiState.formEnabled,
				modifier = Modifier.fillMaxWidth()
				) {
				Text(
					text = stringResource(R.string.save_flight),
					fontSize = 16.sp
				)
			}

			OutlinedButton(
				onClick = { onBackClicked() },
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					text = stringResource(R.string.back),
					fontSize = 16.sp
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightAddForm(
	uiState: FlightAddUIState,
	updateFlightDetails: (FlightForm) -> Unit,
	updateDateVisibility: (Boolean) -> Unit,
	updateTravelClassVisibility: (Boolean) -> Unit,
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
				modifier = modifier,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				OutlinedTextField(
					label = { Text(stringResource(R.string.flight_origin)) },
					value = uiState.flightForm.originAirportString,
					onValueChange = { updateFlightDetails(uiState.flightForm.copy(originAirportString = it)) },
					singleLine = true,
					enabled = uiState.formEnabled,
					shape = shapes.large,
					modifier = Modifier.weight(1f)
				)
				Spacer(modifier = Modifier.width(8.dp))
				OutlinedTextField(
					label = { Text(stringResource(R.string.flight_destination)) },
					value = uiState.flightForm.destinationAirportString,
					onValueChange = { updateFlightDetails(uiState.flightForm.copy(destinationAirportString = it)) },
					singleLine = true,
					enabled = uiState.formEnabled,
					shape = shapes.large,
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
					expanded = uiState.travelClassDropdownExpanded,
					onTravelClassSelected = { updateFlightDetails(uiState.flightForm.copy(travelClass = it)) },
					updateVisibility = updateTravelClassVisibility,
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
			TravelDatePicker(
				currentDepartureDay = uiState.flightForm.departureDay,
				datePickerState = uiState.departureDayPickerState,
				expanded = uiState.departureDayModalVisible,
				onDateSelected = { updateFlightDetails(uiState.flightForm.copy(departureDay = Date(it))) },
				updateVisibility = updateDateVisibility,
				enabled = uiState.formEnabled,
				modifier = modifier
			)
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
			modifier = modifier.menuAnchor().fillMaxWidth(),
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
			modifier = Modifier.exposedDropdownSize().heightIn(250.dp)

		) {
			TravelClass.entries.forEach { travelClass ->
				DropdownMenuItem(
					text = { Text(text = travelClass.name, fontSize = 14.sp) },
					onClick = {
						onTravelClassSelected(travelClass)
						updateVisibility(false)
					},
					modifier = modifier.fillMaxWidth()
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelDatePicker(
	currentDepartureDay: Date,
	datePickerState: DatePickerState,
	expanded: Boolean,
	onDateSelected: (Long) -> Unit,
	updateVisibility: (Boolean) -> Unit,
	enabled: Boolean,
	modifier: Modifier = Modifier
) {
	OutlinedTextFieldButton(
		label = { Text(stringResource(R.string.date)) },
		value = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(currentDepartureDay),
		onClick = { updateVisibility(true) },
		enabled = enabled,
		modifier = modifier,
		trailingIcon = {
			Icons.Default.DateRange
		}
	)
	if (expanded && enabled) {
		DatePickerDialog(
			onDismissRequest = { updateVisibility(false) },
			confirmButton = {
				TextButton(onClick = {
					datePickerState.selectedDateMillis?.let { onDateSelected(it) }
					updateVisibility(false)
				}) {
					Text("OK")
				}
			},
			dismissButton = {
				TextButton(onClick = {
					updateVisibility(false) }
				) {
					Text("Cancel")
				}
			},
		) {
			DatePicker(
				state = datePickerState,
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun FlightAddScreenPreview() {
	FlightAddScreenContent(
		uiState = FlightAddUIState(),
		onBackClicked = {},
		onContinueClicked = {},
		updateDateVisibility = {},
		updateFlightDetails = {},
		updateTravelClassVisibility = {}
	)
}