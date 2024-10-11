package com.cayot.planou.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cayot.planou.R
import com.cayot.planou.ui.flightAdd.FlightAddScreen
import com.cayot.planou.ui.flightDetails.FlightDetailsScreen
import com.cayot.planou.ui.flightList.FlightListScreen

enum class PlanouScreen(@StringRes val title: Int, val argName : String) {
	List(R.string.flight_list, ""),
	Add(R.string.add_flight, ""),
	Details(R.string.flight_details, "flightId")
}

@Composable
fun PlanouNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier
) {
	NavHost(
		navController = navController,
		startDestination = PlanouScreen.List.name,
		modifier = modifier
	) {
		composable(route = PlanouScreen.List.name) {
			FlightListScreen(
				onFlightPressed = {
					navController.navigate(PlanouScreen.Details.name + "/" + it)
				},
				onAddFlightPressed = { navController.navigate(PlanouScreen.Add.name) }
			)
		}
		composable(route = PlanouScreen.Add.name) {
			FlightAddScreen(
				onNavigateUp = { navController.navigateUp() },
				navigateBack = { navController.popBackStack() }
			)
		}
		composable(
			route = "${PlanouScreen.Details.name}/{${PlanouScreen.Details.argName}}",
			arguments = listOf(navArgument(PlanouScreen.Details.argName) {
				type = NavType.IntType
			})
		) {
			FlightDetailsScreen(
				onNavigateUp = { navController.navigateUp() },
				navigateBack = { navController.popBackStack() }
			)
		}
	}
}