package com.uitopic.restockmobile.features.monitoring.presentation.state

import com.uitopic.restockmobile.features.monitoring.domain.model.AdditionalSupply
import com.uitopic.restockmobile.features.monitoring.domain.model.Dish
import com.uitopic.restockmobile.features.monitoring.domain.model.SaleSummary

data class SalesUiState(
    val step: SaleStep = SaleStep.Idle,
    val availableDishes: List<Dish> = emptyList(),
    val availableSupplies: List<AdditionalSupply> = emptyList(),
    val selectedDishes: List<SaleDishSelection> = emptyList(),
    val selectedSupplies: List<SaleSupplySelection> = emptyList(),
    val totalRegisteredSales: Int = 0,
    val lastSaleSummary: SaleSummary? = null
)

data class SaleDishSelection(
    val id: String,
    val dish: Dish,
    val quantity: Int = 1,
    val isChecked: Boolean = true
)

data class SaleSupplySelection(
    val id: String,
    val supply: AdditionalSupply,
    val quantityLabel: String = supply.defaultQuantityLabel,
    val isChecked: Boolean = true
)

sealed class SaleStep {
    data object Idle : SaleStep()
    data object SelectDishes : SaleStep()
    data object SelectSupplies : SaleStep()
    data object Confirmation : SaleStep()
}
