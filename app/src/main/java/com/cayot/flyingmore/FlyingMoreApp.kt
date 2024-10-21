package com.cayot.flyingmore

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cayot.flyingmore.ui.navigation.FlyingMoreNavHost

@Composable
fun	FlyingMoreApp(navController: NavHostController = rememberNavController()) {
	FlyingMoreNavHost(navController = navController)
}
