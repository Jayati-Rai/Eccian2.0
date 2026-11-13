package com.example.project20.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project20.R
import com.example.project20.ui.notifications.NotificationsScreen

private object Routes {
    const val HOME = "home"
    const val NOTES = "notes"
    const val PYQS = "pyqs"
    const val NOTIFICATIONS = "notifications"
    const val SUBJECT = "subject/{selection}/{year}"
    const val RESOURCES = "resources/{selection}/{year}/{subject}"

    fun subject(selection: String, year: String) = "subject/$selection/$year"
    fun resources(selection: String, year: String, subject: String) =
        "resources/$selection/$year/$subject"
}

private data class TopLevelDestination(
    val route: String,
    val label: Int,
    val icon: Int
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val destinations = listOf(
        TopLevelDestination(Routes.HOME, R.string.title_home, R.drawable.ic_home_black_24dp),
        TopLevelDestination(Routes.NOTES, R.string.title_notes, R.drawable.baseline_notes),
        TopLevelDestination(Routes.PYQS, R.string.title_pyqs, R.drawable.ic_dashboard_black_24dp),
        TopLevelDestination(
            Routes.NOTIFICATIONS,
            R.string.title_notifications,
            R.drawable.ic_notifications_black_24dp
        )
    )
    val showBottomBar = destinations.any { it.route == currentRoute }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    destinations.forEach { destination ->
                        NavigationBarItem(
                            selected = currentRoute == destination.route,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(Routes.HOME) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(destination.icon),
                                    contentDescription = stringResource(destination.label)
                                )
                            },
                            label = { Text(stringResource(destination.label)) }
                        )
                    }
                }
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen()
            }
            composable(Routes.NOTES) {
                YearSelectionScreen(
                    resourceType = "notes",
                    title = "Study Notes",
                    onYearSelected = { year ->
                        navController.navigate(Routes.subject("notes", year))
                    }
                )
            }
            composable(Routes.PYQS) {
                YearSelectionScreen(
                    resourceType = "pyqs",
                    title = "Previous Year Question Papers",
                    onYearSelected = { year ->
                        navController.navigate(Routes.subject("pyqs", year))
                    }
                )
            }
            composable(
                route = Routes.SUBJECT,
                arguments = listOf(
                    navArgument("selection") { type = NavType.StringType },
                    navArgument("year") { type = NavType.StringType }
                )
            ) { entry ->
                val selection = entry.arguments?.getString("selection").orEmpty()
                val year = entry.arguments?.getString("year").orEmpty()
                SubjectSelectionScreen(
                    selection = selection,
                    year = year,
                    onSubjectSelected = { subject ->
                        navController.navigate(Routes.resources(selection, year, subject))
                    }
                )
            }
            composable(
                route = Routes.RESOURCES,
                arguments = listOf(
                    navArgument("selection") { type = NavType.StringType },
                    navArgument("year") { type = NavType.StringType },
                    navArgument("subject") { type = NavType.StringType }
                )
            ) { entry ->
                val selection = entry.arguments?.getString("selection").orEmpty()
                val year = entry.arguments?.getString("year").orEmpty()
                val subject = entry.arguments?.getString("subject").orEmpty()
                PdfResourcesScreen(path = "$selection/$year/$subject")
            }
            composable(Routes.NOTIFICATIONS) {
                NotificationsScreen()
            }
        }
    }
}
