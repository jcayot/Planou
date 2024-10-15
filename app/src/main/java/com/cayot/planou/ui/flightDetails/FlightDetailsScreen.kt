package com.cayot.planou.ui.flightDetails

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.planou.R
import com.cayot.planou.data.FlightMapState
import com.cayot.planou.data.airport.Airport
import com.cayot.planou.data.flight.Flight
import com.cayot.planou.data.flight.getArrivalTimeString
import com.cayot.planou.data.flight.getDepartureDateString
import com.cayot.planou.data.flight.getDepartureTimeString
import com.cayot.planou.data.flight.getDistanceString
import com.cayot.planou.ui.AppViewModelProvider
import com.cayot.planou.ui.PlanouTopBar
import com.cayot.planou.ui.composable.FlightMap
import com.cayot.planou.ui.composable.LabelledData
import com.cayot.planou.ui.composable.composableBitmap
import com.cayot.planou.ui.navigation.PlanouScreen
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
	navigateBack: () -> Unit,
	onNavigateUp: () -> Unit,
	modifier: Modifier = Modifier
) {
	val viewModel: FlightDetailsViewModel = viewModel(factory = AppViewModelProvider.factory)
	val uiState by viewModel.uiState.collectAsState()
	val context = LocalContext.current

	if (uiState.flight != null) {
		LaunchedEffect (Unit) {
			viewModel.shareCard.collect {
				shareFlight(context, it, uiState.flight!!.originAirportCode, uiState.flight!!.destinationAirportCode)
			}
		}
	}

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
			onBackPressed = navigateBack,
			updateNotesVisibility = viewModel::updateNotesVisibility,
			onSharePressed = viewModel::shareFlightCard,
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
	updateNotesVisibility: () -> Unit,
	onBackPressed: () -> Unit,
	onSharePressed: (Bitmap) -> Unit,
	modifier: Modifier = Modifier
) {
	if (uiState.flight != null) {
		FlightDetails(
			flight = uiState.flight,
			originAirport = uiState.retrievedOriginAirport,
			destinationAirport = uiState.retrievedDestinationAirport,
			flightMapState = uiState.flightMapState,
			notesVisible = uiState.notesVisible,
			onBackPressed = onBackPressed,
			onSharePressed = onSharePressed,
			updateNotesVisibility = updateNotesVisibility,
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
	notesVisible: Boolean,
	updateNotesVisibility: () -> Unit,
	onBackPressed: () -> Unit,
	onSharePressed: (Bitmap) -> Unit,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = dimensionResource(id = R.dimen.padding_smadium)),
		verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_smadium)),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		val flightCard = composableBitmap { FlightCard(
			flight = flight,
			originAirport = originAirport,
			destinationAirport = destinationAirport,
			flightMapState = flightMapState,
		) }
		FlightNotes(
			notesVisible = notesVisible,
			updateNotesVisibility = updateNotesVisibility,
			modifier = Modifier.fillMaxWidth()
		)
		Button(
			onClick = {
				onSharePressed(flightCard.invoke())
			},
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = stringResource(R.string.share_flight),
				fontSize = 16.sp
			)
		}
		OutlinedButton(
			onClick = onBackPressed,
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = stringResource(R.string.back),
				fontSize = 16.sp
			)
		}
	}
}

@Composable
fun FlightCard(
	flight: Flight,
	originAirport: Airport?,
	destinationAirport: Airport?,
	flightMapState: FlightMapState?,
	modifier: Modifier = Modifier,
	elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
) {
	Card(
		elevation = elevation,
		modifier = modifier
	) {
		Column (
			modifier = Modifier.padding(dimensionResource(R.dimen.padding_smadium)),
			verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_smadium))
		){
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.height(200.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))
			) {
				if (flightMapState != null) {
					FlightMap(
						flightMapState = flightMapState,
					)
				}
			}
			Row (
				modifier = Modifier.fillMaxWidth()
			){
				Text(
					text = flight.airline,
					style = typography.headlineMedium,
					overflow = TextOverflow.Ellipsis,
					fontWeight = FontWeight.SemiBold
				)
				Spacer(modifier = modifier.weight(1f))
				LabelledData(
					horizontalAlignment = Alignment.CenterHorizontally,
					labelText = stringResource(R.string.date),
					labelStyle = typography.labelSmall,
					dataText = flight.getDepartureDateString(),
					dataStyle = typography.bodyMedium,
					dataWeight = FontWeight.SemiBold,
				)
			}
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
					labelText = stringResource(R.string.plane_model) + " :",
					dataText = flight.planeModel,
					dataStyle = typography.headlineSmall,
					dataWeight = FontWeight.SemiBold,
					dataOverflow = TextOverflow.Ellipsis,
					modifier = Modifier.weight(4f)
				)
				Spacer(modifier.weight(1f))
				LabelledData (
					horizontalAlignment = Alignment.End,
					labelText = stringResource(R.string.travel_class) + " :",
					dataText = flight.travelClass.name,
					dataStyle = typography.headlineSmall,
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
					labelText = stringResource(R.string.departure_time),
					labelStyle = typography.labelSmall,
					dataText = flight.getDepartureTimeString(),
					dataStyle = typography.bodyMedium,
					dataWeight = FontWeight.SemiBold,
					modifier = Modifier.weight(1f)
				)
				LabelledData(
					horizontalAlignment = Alignment.CenterHorizontally,
					labelText = stringResource(R.string.arrival_time),
					labelStyle = typography.labelSmall,
					dataText = flight.getArrivalTimeString(),
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
					labelText = stringResource(R.string.seat),
					labelStyle = typography.labelSmall,
					dataText = "-",
					dataStyle = typography.bodyMedium,
					dataWeight = FontWeight.SemiBold,
					modifier = Modifier.weight(1f)
				)
			}
		}
	}
}

@Composable
fun FlightNotes(
	notesVisible: Boolean,
	updateNotesVisibility: () -> Unit,
	modifier: Modifier = Modifier
) {
	Row (
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier.height(25.dp)
			.clickable { updateNotesVisibility() }
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.fillMaxHeight()
		) {
			Text(
				text = stringResource(R.string.notes),
				textAlign = TextAlign.Start,
				style = typography.bodyLarge,
			)
		}
		Icon(
			Icons.Filled.ArrowDropDown,
			null,
			Modifier.rotate(if (notesVisible) 0f else 270f)
				.fillMaxHeight()
		)
	}
	AnimatedVisibility(notesVisible) {
		Text(
			text = "Notes",
			modifier = modifier.fillMaxWidth()
		)
	}
}

private fun shareFlight(context: Context, flightCardUri: Uri, departureCode: String, arrivalCode: String) {
	val intent = Intent(Intent.ACTION_SEND).apply {
		putExtra(Intent.EXTRA_TITLE, "Check my flight from $departureCode to $arrivalCode")
		putExtra(Intent.EXTRA_STREAM, flightCardUri)
		type = "image/png"
	}
	context.startActivity(intent)
}

@Preview
@Composable
fun FlightDetailsPreview() {
	FlightDetails(
		flight = Flight.getPlaceholderFlight(),
		originAirport = Airport.getCDG(),
		destinationAirport = Airport.getHEL(),
		flightMapState = null,
		onBackPressed = {},
		updateNotesVisibility = {},
		onSharePressed = {},
		notesVisible = false
	)
}