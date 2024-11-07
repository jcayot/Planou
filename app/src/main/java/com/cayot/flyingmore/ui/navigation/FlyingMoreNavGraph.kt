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
import com.cayot.flyingmore.ui.flightEdit.FlightEditScreen
import com.cayot.flyingmore.ui.flightDetails.FlightDetailsScreen
import com.cayot.flyingmore.ui.flightList.FlightListScreen

enum class FlyingMoreScreen(@StringRes val title: Int, val argName : String) {
	List(R.string.flight_list, ""),
	Add(R.string.add_flight, ""),
	Edit(R.string.edit_flight, "flightId"),
	Details(R.string.flight_details, "flightId")
}

@Composable
fun FlyingMoreNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier
) {
	NavHost(
		navController = navController,
		startDestination = FlyingMoreScreen.List.name,
		modifier = modifier
	) {
		composable(route = FlyingMoreScreen.List.name) {
			FlightListScreen(
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
				navigateHome = { navController.navigate(FlyingMoreScreen.List.name) }
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
				navigateHome = { navController.navigate(FlyingMoreScreen.List.name) }
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