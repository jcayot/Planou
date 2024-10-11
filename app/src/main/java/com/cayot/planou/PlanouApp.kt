package com.cayot.planou

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cayot.planou.ui.navigation.PlanouNavHost

@Composable
fun	PlanouApp(navController: NavHostController = rememberNavController()) {
	PlanouNavHost(navController = navController)
}
