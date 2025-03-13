package com.cayot.flyingmore.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.MultilineChart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.cayot.flyingmore.R
import com.cayot.flyingmore.ui.PlanouTopBar
import com.cayot.flyingmore.ui.home.list.HomeListTab
import com.cayot.flyingmore.ui.home.statistics.HomeStatisticsTab

enum class HomeScreenContent(@StringRes val title: Int, val icon: ImageVector, @StringRes val longTitle: Int) {
    List(R.string.list, Icons.AutoMirrored.Filled.List, R.string.flight_list),
    Statistics(R.string.statistics, Icons.AutoMirrored.Filled.MultilineChart, R.string.flying_statistics)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onFlightPressed: (Int) -> Unit,
    onAddFlightPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentScreen: HomeScreenContent by remember { mutableStateOf(HomeScreenContent.List) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            PlanouTopBar(
                title = stringResource(currentScreen.longTitle),
                canNavigateBack = false,
                actions = {
                    if (currentScreen == HomeScreenContent.List)
                        AddFlightAction(onClicked =  onAddFlightPressed)
                }
            )
        },
        bottomBar = {
            HomeBottomNavigation(
                currentTab = currentScreen,
                onTabPressed = { currentScreen = it },
                tabList = HomeScreenContent.entries,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        when (currentScreen) {
            HomeScreenContent.List ->
                HomeListTab(
                    onFlightPressed = onFlightPressed,
                    modifier = modifier.fillMaxSize(),
                    contentPadding = innerPadding
                )
            HomeScreenContent.Statistics ->
                HomeStatisticsTab(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = innerPadding
                )
        }
    }
}

@Composable
fun HomeBottomNavigation(
    currentTab: HomeScreenContent,
    onTabPressed: (HomeScreenContent) -> Unit,
    tabList: List<HomeScreenContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        for (tab in tabList) {
            NavigationBarItem(
                selected = tab == currentTab,
                onClick = { onTabPressed(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(tab.title)
                    )
                },
                label = {
                    Text(
                        stringResource(tab.title)
                    )
                }
            )
        }
    }
}

@Composable
fun AddFlightAction(
    onClicked: () -> Unit
) {
    IconButton(
        onClick = onClicked
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = Icons.Filled.Add.name
        )
    }
}
