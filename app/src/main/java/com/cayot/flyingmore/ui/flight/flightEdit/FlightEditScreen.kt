package com.cayot.flyingmore.ui.flight.flightEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.DayDifference
import com.cayot.flyingmore.data.TravelClass
import com.cayot.flyingmore.data.airport.Airport
import com.cayot.flyingmore.ui.AppViewModelProvider
import com.cayot.flyingmore.ui.PlanouTopBar
import com.cayot.flyingmore.ui.composable.OutlinedDatePicker
import com.cayot.flyingmore.ui.composable.OutlinedTimePicker
import com.cayot.flyingmore.ui.composable.SearchTextField
import com.cayot.flyingmore.ui.composable.SelectDropdown
import com.cayot.flyingmore.ui.theme.Typography
import com.cayot.flyingmore.utils.SelectableDatesTo
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightEditScreen(
	title: String,
	navigateBack: () -> Unit,
	onNavigateUp: () -> Unit,
	canNavigateBack: Boolean = true,
	navigateHome: () -> Unit
) {
	val	viewModel: FlightEditViewModel = viewModel(factory = AppViewModelProvider.factory)
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.navigateBack.collect{ navigateBack() }
	}

	LaunchedEffect(Unit) {
		viewModel.navigateHome.collect{ navigateHome() }
	}

	Scaffold (
		topBar = {
			PlanouTopBar(
				title = title + " " + stringResource(R.string.flight),
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
			searchDepartureDismissed = viewModel::departureAirportDismissed,
			searchArrivalAirport = viewModel::searchArrivalAirport,
			arrivalAirportSelected = viewModel::arrivalAirportSelected,
			searchArrivalDismissed = viewModel::arrivalAirportDismissed,
			updateFormElementVisibility = viewModel::updateFormElementVisibility,
			onSaveClicked = viewModel::saveFlight,
			onDeleteClicked = viewModel::deleteFlight,
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
	searchDepartureDismissed: (String, List<Airport>) -> Unit = { _: String, _: List<Airport> -> },
	searchArrivalAirport: (String) -> Unit = {},
	arrivalAirportSelected: (Airport) -> Unit = {},
	searchArrivalDismissed: (String, List<Airport>) -> Unit = { _: String, _: List<Airport> -> },
	updateFormElementVisibility: (FormElementVisibility) -> Unit = {},
	onSaveClicked: () -> Unit = {},
	onDeleteClicked: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	val scrollState = rememberScrollState()

	Column (
		verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
			.padding(dimensionResource(id = R.dimen.padding_medium))
			.verticalScroll(scrollState)
			.imePadding()
	) {
		FlightEditForm(
			uiState = uiState,
			updateFlightDetails = updateFlightDetails,
			searchDepartureAirport = searchDepartureAirport,
			departureAirportSelected = departureAirportSelected,
			searchDepartureDismissed = searchDepartureDismissed,
			searchArrivalAirport = searchArrivalAirport,
			arrivalAirportSelected = arrivalAirportSelected,
			searchArrivalDismissed = searchArrivalDismissed,
			updateFormElementVisibility = updateFormElementVisibility,
			modifier = Modifier.fillMaxWidth()
		)
		Button(
			onClick = { onSaveClicked() },
			enabled = uiState.isEntryValid && uiState.formEnabled,
		) {
			Text(
				text = stringResource(R.string.save_flight),
				fontSize = 16.sp,
				modifier = Modifier.padding(dimensionResource(R.dimen.padding_mini))
			)
		}
		if (uiState.canDelete) {
			TextButton(
				onClick = { onDeleteClicked() },
				enabled = uiState.formEnabled,
			) {
				Text(
					text = stringResource(R.string.delete_flight),
					fontSize = 14.sp,
					color = Color.Red
				)
			}
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
	searchDepartureDismissed: (String, List<Airport>) -> Unit,
	searchArrivalAirport: (String) -> Unit,
	arrivalAirportSelected: (Airport) -> Unit,
	searchArrivalDismissed: (String, List<Airport>) -> Unit,
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
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				SearchTextField(
					labelText = stringResource(R.string.flight_origin),
					value = uiState.flightForm.originAirportString,
					items = uiState.flightForm.foundOriginAirportsList,
					onValueChange = searchDepartureAirport,
					onItemClicked = departureAirportSelected,
					onDismissed = searchDepartureDismissed,
					updateVisibility = {
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							originAirportDropdownVisible = it
						))
					},
					expanded = uiState.formElementVisibility.originAirportDropdownVisible,
					enabled = uiState.formEnabled,
					keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
					modifier = Modifier.weight(1f)
				)
				Spacer(modifier = Modifier.width(8.dp))
				SearchTextField(
					labelText = stringResource(R.string.flight_destination),
					value = uiState.flightForm.destinationAirportString,
					items = uiState.flightForm.foundDestinationAirportList,
					onValueChange = searchArrivalAirport,
					onItemClicked = arrivalAirportSelected,
					onDismissed = searchArrivalDismissed,
					updateVisibility = {
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							destinationAirportDropdownVisible = it
						))
					},
					expanded = uiState.formElementVisibility.destinationAirportDropdownVisible,
					enabled = uiState.formEnabled,
					keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
				keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
				modifier = modifier
			)
			Row (
				modifier = modifier,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				SelectDropdown(
					labelText = stringResource(R.string.travel_class),
					items = TravelClass.entries.toList(),
					selected = uiState.flightForm.travelClass,
					onItemSelected = { updateFlightDetails(uiState.flightForm.copy(travelClass = it))},
					updateVisibility = { visibility ->
						updateFormElementVisibility(uiState.formElementVisibility.copy(
							travelClassDropdownVisible = visibility
						))
					},
					expanded = uiState.formElementVisibility.travelClassDropdownVisible,
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
					keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
					modifier = Modifier.weight(1f)
				)

			}
			Row (
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
				modifier = modifier
			) {
				OutlinedTextField(
					label = { Text(stringResource(R.string.plane_model)) },
					value = uiState.flightForm.planeModel,
					onValueChange = { updateFlightDetails(uiState.flightForm.copy(planeModel = it)) },
					singleLine = true,
					enabled = uiState.formEnabled,
					shape = shapes.large,
					keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
					modifier = Modifier.weight(3f)
				)
				OutlinedTextField(
					label = { Text(stringResource(R.string.seat)) },
					value = uiState.flightForm.seatNumber,
					onValueChange = { updateFlightDetails(uiState.flightForm.copy(seatNumber = it)) },
					singleLine = true,
					enabled = uiState.formEnabled,
					shape = shapes.large,
					keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
					modifier = Modifier.weight(2f)
					)
			}
			Row (
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
				modifier = modifier
			) {
				OutlinedDatePicker(
					selectedDate = uiState.flightForm.departureDate,
					visible = uiState.formElementVisibility.departureDayModalVisible,
					selectableDates = SelectableDatesTo(Instant.now()),
					labelText = stringResource(R.string.departure_day),
					onDateSelected = { date ->
						updateFlightDetails(uiState.flightForm.copy(departureDate = date))
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
					selectedHour = uiState.flightForm.departureHour,
					selectedMinute = uiState.flightForm.departureMinute,
					visible = uiState.formElementVisibility.departureTimeModalVisible,
					labelText = stringResource(R.string.departure_time),
					onTimeSelected = { hour, minute ->
						updateFlightDetails(uiState.flightForm.copy(
							departureHour = hour,
							departureMinute = minute
						))
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
				Column (
					modifier = Modifier.weight(3f),
				) {
					Text(
						text = stringResource(R.string.day_difference),
						style = Typography.bodySmall
					)
					SingleChoiceSegmentedButtonRow(
						modifier = Modifier.height(OutlinedTextFieldDefaults.MinHeight)
					) {
						DayDifference.entries.forEachIndexed { index, dayDifference ->
							SegmentedButton(
								label = { Text("+${dayDifference.value}") },
								shape = SegmentedButtonDefaults.itemShape(
									index = index,
									count = DayDifference.entries.size
								),
								onClick = { updateFlightDetails(uiState.flightForm.copy(dayDifference = dayDifference)) },
								selected = uiState.flightForm.dayDifference == dayDifference,
								enabled = uiState.formEnabled,
							)
						}
					}
				}
				OutlinedTimePicker(
					selectedHour = uiState.flightForm.arrivalHour,
					selectedMinute = uiState.flightForm.arrivalMinute,
					visible = uiState.formElementVisibility.arrivalTimeModalVisible,
					labelText = stringResource(R.string.arrival_time),
					onTimeSelected = { hour, minute ->
						updateFlightDetails(uiState.flightForm.copy(
							arrivalHour = hour,
							arrivalMinute = minute
						))
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

@Preview(showBackground = true)
@Composable
fun FlightAddScreenPreview() {
	FlightEditScreenContent(
		uiState = FlightEditUIState(),
    )
}