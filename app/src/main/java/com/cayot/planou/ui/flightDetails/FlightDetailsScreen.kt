package com.cayot.planou.ui.flightDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.planou.R
import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.flight.getDepartureDateString
import com.cayot.planou.data.flight.getDepartureTimeString
import com.cayot.planou.data.flight.getDistanceString
import com.cayot.planou.ui.AppViewModelProvider
import com.cayot.planou.ui.PlanouTopBar
import com.cayot.planou.ui.composable.FlightMap
import com.cayot.planou.ui.navigation.PlanouScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
	navigateBack: () -> Unit,
	onNavigateUp: () -> Unit,
	modifier: Modifier = Modifier
) {
	val viewModel: FlightDetailsViewModel = viewModel(factory = AppViewModelProvider.factory)
	val uiState by viewModel.uiState.collectAsState()

	Scaffold (
		topBar = {
			PlanouTopBar(
				title = stringResource(PlanouScreen.Details.title),
				canNavigateBack = true,
				navigateUp = onNavigateUp
			)
		}
	) { innerPadding ->
		FlightDetailsScreenContent(
			uiState = uiState,
			modifier = modifier
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
fun FlightDetailsScreenContent(
	uiState: FlightDetailsUIState,
	modifier: Modifier = Modifier
) {
	if (uiState.flight != null) {
		FlightDetails(
			flight = uiState.flight,
			originAirport = uiState.retrievedOriginAirport,
			destinationAirport = uiState.retrievedDestinationAirport,
			flightMapState = uiState.flightMapState,
			modifier = modifier
		)
	} else if (uiState.isRetrievingFlight) {
		Box(
			modifier = modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			Column (
				modifier = Modifier.padding(dimensionResource(R.dimen.padding_smadium)),
				verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_smadium)),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				CircularProgressIndicator(
					modifier = Modifier.size(50.dp)
				)
				Text(
					text = stringResource(R.string.loading_flight_data),
					style = typography.titleLarge
				)
			}
		}
	} else {
		Box(
			modifier = modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			Column (
				modifier = Modifier.padding(dimensionResource(R.dimen.padding_smadium)),
				verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_smadium)),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = stringResource(R.string.error_retriving_flight),
					style = typography.titleLarge
				)
			}
		}
	}
}

@Composable
fun FlightDetails(
	flight: Flight,
	originAirport: Airport?,
	destinationAirport: Airport?,
	flightMapState: FlightMapState?,
	modifier: Modifier = Modifier
) {
	Card(
		elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
		modifier = modifier.padding(dimensionResource(R.dimen.padding_smadium))
	) {
		Column (
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_smadium)),
			verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_smadium))
		){
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.height(200.dp)
			) {
				if (flightMapState != null) {
					FlightMap(
						flightMapState = flightMapState,
					)
				} else {
					Text(
						text = stringResource(R.string.error_retriving_airport),
						style = typography.titleLarge
					)
				}
			}
			Text(
				text = stringResource(R.string.flight_informations),
				style = typography.titleMedium
			)
			Row (
				modifier = Modifier.fillMaxWidth()
					.height(64.dp)
			){
				LabelledData(
					horizontalAlignment = Alignment.Start,
					labelText = originAirport?.municipality ?: stringResource(R.string.error_retriving_airport),
					dataText = originAirport?.iataCode ?: flight.originAirportCode,
					dataStyle = typography.displayMedium,
					modifier = Modifier.weight(1f)
						.fillMaxHeight())
				Column (
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier.weight(1f)
						.fillMaxHeight()
				){
					Text(
						text = flight.flightNumber,
						style = typography.labelMedium,
						maxLines = 1,
						modifier = Modifier.weight(1f)
							.padding(2.dp)
					)
					Image(
						painter = painterResource(R.drawable.flight_24px),
						contentDescription = stringResource(R.string.plane_right_icon),
						alignment = Alignment.Center,
						contentScale = ContentScale.FillHeight,
						modifier = Modifier
							.weight(2f)
					)
				}
				LabelledData(
					horizontalAlignment = Alignment.End,
					labelText = destinationAirport?.municipality ?: stringResource(R.string.error_retriving_airport),
					dataText = destinationAirport?.iataCode ?: flight.destinationAirportCode,
					dataStyle = typography.displayMedium,
					modifier = Modifier.weight(1f)
						.fillMaxHeight())
			}
			Row (
				modifier = Modifier.fillMaxWidth()
					.height(54.dp)
			) {
				LabelledData (
					horizontalAlignment = Alignment.Start,
					labelText = stringResource(R.string.flight_airline) + " :",
					dataText = flight.airline,
					dataStyle = typography.headlineMedium,
					dataWeight = FontWeight.SemiBold,
					dataOverflow = TextOverflow.Ellipsis,
					modifier = Modifier.weight(4f)
				)
				Spacer(modifier.weight(1f))
				LabelledData (
					horizontalAlignment = Alignment.End,
					labelText = stringResource(R.string.plane_model) + " :",
					dataText = flight.planeModel,
					dataStyle = typography.headlineMedium,
					dataWeight = FontWeight.SemiBold,
					dataOverflow = TextOverflow.Ellipsis,
					modifier = Modifier.weight(4f)
				)
			}
			Row (
				modifier = Modifier.fillMaxWidth()
			) {
				LabelledData(
					horizontalAlignment = Alignment.CenterHorizontally,
					labelText = stringResource(R.string.departure_day),
					labelStyle = typography.labelSmall,
					dataText = flight.getDepartureDateString(),
					dataStyle = typography.bodyMedium,
					dataWeight = FontWeight.SemiBold,
					modifier = Modifier.weight(1f)
				)
				LabelledData(
					horizontalAlignment = Alignment.CenterHorizontally,
					labelText = stringResource(R.string.departure_time),
					labelStyle = typography.labelSmall,
					dataText = flight.getDepartureTimeString(),
					dataStyle = typography.bodyMedium,
					dataWeight = FontWeight.SemiBold,
					modifier = Modifier.weight(1f)
				)
				LabelledData(
					horizontalAlignment = Alignment.CenterHorizontally,
					labelText = stringResource(R.string.distance),
					labelStyle = typography.labelSmall,
					dataText = flight.getDistanceString(),
					dataStyle = typography.bodyMedium,
					dataWeight = FontWeight.SemiBold,
					modifier = Modifier.weight(1f)
				)
				LabelledData(
					horizontalAlignment = Alignment.CenterHorizontally,
					labelText = stringResource(R.string.travel_class),
					labelStyle = typography.labelSmall,
					dataText = flight.travelClass.name,
					dataStyle = typography.bodyMedium,
					dataWeight = FontWeight.SemiBold,
					modifier = Modifier.weight(1f)
				)
			}
		}
	}
}

@Composable
fun LabelledData(
	horizontalAlignment: Alignment.Horizontal,
	labelText: String,
	dataText: String,
	dataStyle: TextStyle,
	modifier: Modifier = Modifier,
	dataWeight: FontWeight? = dataStyle.fontWeight,
	dataOverflow: TextOverflow = TextOverflow.Clip,
	labelStyle: TextStyle = typography.titleSmall
) {
	Column (
		horizontalAlignment = horizontalAlignment,
		modifier = modifier
	) {
		Text(
			text = labelText,
			style = labelStyle,
			maxLines = 1
		)
		Text(
			text = dataText,
			style = dataStyle,
			fontWeight = dataWeight,
			overflow = dataOverflow,
			maxLines = 1
		)
	}
}

@Preview
@Composable
fun FlightDetailsPreview() {
	FlightDetails(
		flight = Flight.getPlaceholderFlight(),
		originAirport = Airport.getCDG(),
		destinationAirport = Airport.getHEL(),
		flightMapState = null
	)

}