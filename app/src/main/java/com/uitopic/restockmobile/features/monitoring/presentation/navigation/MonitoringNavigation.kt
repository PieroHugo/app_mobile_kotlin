package com.uitopic.restockmobile.features.monitoring.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.uitopic.restockmobile.features.monitoring.presentation.screens.SalesMonitoringRoute

sealed class MonitoringRoute(val route: String) {
    data object Sales : MonitoringRoute("monitoring_sales")
}

fun NavGraphBuilder.monitoringNavGraph() {
    navigation(
        startDestination = MonitoringRoute.Sales.route,
        route = "monitoring_graph"
    ) {
        composable(MonitoringRoute.Sales.route) {
            SalesMonitoringRoute()
        }
    }
}

