package com.cayot.flyingmore.ui.home.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cayot.flyingmore.R
import com.cayot.flyingmore.data.FlightMapState
import com.cayot.flyingmore.data.flight.FlightBrief
import com.cayot.flyingmore.data.flight.getDepartureDateString
import com.cayot.flyingmore.data.flight.getDistanceString
import com.cayot.flyingmore.ui.AppViewModelProvider
import com.cayot.flyingmore.ui.composable.FlightMap

@Composable
fun HomeListTab(
    onFlightPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel: FlightListViewModel = viewModel(factory = AppViewModelProvider.factory)
    val uiState by viewModel.uiState.collectAsState()

    FlightListScreenContent(
        uiState = uiState,
        onFlightPressed = onFlightPressed,
        onItemVisible = viewModel::updateFlightMap,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    )
}

@Composable
fun FlightListScreenContent(
    uiState: FlightListUIState,
    onFlightPressed: (Int) -> Unit,
    onItemVisible: (FlightBrief) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (uiState.flightList != null) {
        if (uiState.flightList.isEmpty()) {
            EmptyFlightListPlaceholder(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            )
        } else {
            FlightListList(
                flightList = uiState.flightList,
                flightMapStateMap = uiState.flightMapStateMap,
                onFlightPressed = onFlightPressed,
                onItemVisible = onItemVisible,
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
fun EmptyFlightListPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.airplanemode_inactive_40px),
            contentDescription = stringResource(R.string.list_empty_sad_plane),
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = stringResource(R.string.flight_list_empty),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
fun FlightListList(
    flightList: List<FlightItem>,
    flightMapStateMap: Map<Int, FlightMapState?> = emptyMap(),
    onFlightPressed: (Int) -> Unit = {},
    onItemVisible: (FlightBrief) -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn (
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_smadium)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_smadium)),
    ) {
        items(flightList) { flightItem: FlightItem ->
            if (flightItem.year != null) {
                Text(
                    text = flightItem.year.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_large))
                        .padding(bottom = dimensionResource(R.dimen.padding_small))
                )
            } else if (flightItem.flight != null) {
                onItemVisible(flightItem.flight)

                FlightListItemComposable(
                    flight = flightItem.flight,
                    flightMapState = flightMapStateMap[flightItem.flight.id],
                    onFlightPressed = onFlightPressed
                )
            }
        }
    }
}

@Composable
fun FlightListItemComposable(
    flight : FlightBrief,
    flightMapState: FlightMapState? = null,
    onFlightPressed: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onFlightPressed(flight.id) },
        modifier = modifier
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .size(dimensionResource(R.dimen.flight_item_height))
            .padding(dimensionResource(R.dimen.padding_small))
        ) {
            FlightItemMap(
                flightMapState = flightMapState,
                modifier = Modifier.fillMaxHeight()
                    .aspectRatio(1f)
            )
            Column (
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_smadium))
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Badge(containerColor = colorResource(R.color.flight_distance_green),
                        contentColor = Color.White) {
                        Text(
                            text = "+ " + flight.getDistanceString(),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    Spacer(
                        Modifier.weight(1f)
                    )
                    Text(
                        text = flight.getDepartureDateString()
                    )
                }
                Text(
                    text = flight.originAirport.iataCode + " - " + flight.destinationAirport.iataCode,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(
                    Modifier.weight(1f)
                )
                Row {
                    Text(
                        text = flight.airline,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(
                        Modifier.weight(1f)
                    )
                    Text(
                        text = flight.flightNumber,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun FlightItemMap(
    flightMapState: FlightMapState?,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
    ) {
        if (flightMapState != null) {
            FlightMap(
                flightMapState = flightMapState,
                padding = 10
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun	FlightListScreenContentPreview() {
    FlightListList(
        flightList = makeFlightItemsList(listOf(
            FlightBrief.getPlaceholder1(),
            FlightBrief.getPlaceholder1(),
        )),
    )
}

@Preview (showBackground = true)
@Composable
fun EmptyFlightListPreview() {
    EmptyFlightListPlaceholder()
}

@Preview
@Composable
fun FlightListItemPreview() {
    FlightListItemComposable(
        flight = FlightBrief.getPlaceholder1(),
    )
}