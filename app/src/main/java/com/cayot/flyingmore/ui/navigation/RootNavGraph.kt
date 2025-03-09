package com.cayot.flyingmore.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cayot.flyingmore.R
import com.cayot.flyingmore.ui.flight.details.FlightDetailsScreen
import com.cayot.flyingmore.ui.flight.edit.FlightEditScreen
import com.cayot.flyingmore.ui.home.HomeScreen

enum class FlyingMoreScreen(@StringRes val title: Int, val argName : String) {
	Home(R.string.home, ""),
	Add(R.string.add_flight, ""),
	Edit(R.string.edit_flight, "flightId"),
	Details(R.string.flight_details, "flightId"),
}

@Composable
fun FlyingMoreNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier
) {
	NavHost(
		navController = navController,
		startDestination = FlyingMoreScreen.Home.name,
		modifier = modifier
	) {
		composable(route = FlyingMoreScreen.Home.name) {
			HomeScreen(
				onFlightPressed = {
					navController.navigate(FlyingMoreScreen.Details.name + "/" + it)
				},
				onAddFlightPressed = { navController.navigate(FlyingMoreScreen.Add.name) }
			)
		}
		composable(route = FlyingMoreScreen.Add.name) {
			FlightEditScreen(
				title = FlyingMoreScreen.Add.name,
				navigateBack = { navController.popBackStack() },
				onNavigateUp = { navController.navigateUp() },
				navigateHome = { navController.navigate(FlyingMoreScreen.Home.name) }
			)
		}
		composable(
			route = "${FlyingMoreScreen.Edit.name}/{${FlyingMoreScreen.Edit.argName}}",
			arguments = listOf(navArgument(FlyingMoreScreen.Edit.argName) {
				type = NavType.IntType
			})
		) {
			FlightEditScreen(
				title = FlyingMoreScreen.Edit.name,
				onNavigateUp = { navController.navigateUp() },
				navigateBack = { navController.popBackStack() },
				navigateHome = { navController.navigate(FlyingMoreScreen.Home.name) }
			)
		}
		composable(
			route = "${FlyingMoreScreen.Details.name}/{${FlyingMoreScreen.Details.argName}}",
			arguments = listOf(navArgument(FlyingMoreScreen.Details.argName) {
				type = NavType.IntType
			})
		) {
			FlightDetailsScreen(
				editFlight = {
					navController.navigate(FlyingMoreScreen.Edit.name + "/" + it)
				},
				onNavigateUp = { navController.navigateUp() }
			)
		}
	}
}