package com.uitopic.restockmobile.features.monitoring.presentation.states

import com.uitopic.restockmobile.features.monitoring.domain.models.DishSelection
import com.uitopic.restockmobile.features.monitoring.domain.models.SaleRecord
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesDish
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesSupply
import com.uitopic.restockmobile.features.monitoring.domain.models.SupplySelection

enum class SalesStep {
    EMPTY,
    DISHES,
    SUPPLIES,
    SUCCESS
}

data class MonitoringUiState(
    val currentStep: SalesStep = SalesStep.EMPTY,
    val availableDishes: List<SalesDish> = emptyList(),
    val availableSupplies: List<SalesSupply> = emptyList(),
    val selectedDishes: List<DishSelection> = emptyList(),
    val selectedSupplies: List<SupplySelection> = emptyList(),
    val selectedDishId: String? = null,
    val selectedSupplyId: String? = null,
    val lastRegisteredSale: SaleRecord? = null
) {
    val hasSelectedDishes: Boolean = selectedDishes.any { it.isConfirmed }
    val hasSelectedSupplies: Boolean = selectedSupplies.any { it.isConfirmed }
}

