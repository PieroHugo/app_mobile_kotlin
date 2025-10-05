package com.uitopic.restockmobile.features.monitoring.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uitopic.restockmobile.features.monitoring.domain.model.SaleDishSummary
import com.uitopic.restockmobile.features.monitoring.domain.model.SaleSummary
import com.uitopic.restockmobile.features.monitoring.domain.model.SaleSupplySummary
import com.uitopic.restockmobile.features.monitoring.domain.repository.MonitoringRepository
import com.uitopic.restockmobile.features.monitoring.presentation.state.SaleDishSelection
import com.uitopic.restockmobile.features.monitoring.presentation.state.SaleStep
import com.uitopic.restockmobile.features.monitoring.presentation.state.SaleSupplySelection
import com.uitopic.restockmobile.features.monitoring.presentation.state.SalesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: MonitoringRepository
) : ViewModel() {

    var uiState by mutableStateOf(SalesUiState())
        private set

    init {
        uiState = uiState.copy(
            availableDishes = repository.getDishes(),
            availableSupplies = repository.getAdditionalSupplies()
        )
    }

    fun startNewSale() {
        uiState = uiState.copy(
            step = SaleStep.SelectDishes,
            selectedDishes = emptyList(),
            selectedSupplies = emptyList(),
            lastSaleSummary = null
        )
    }

    fun cancelSale() {
        uiState = uiState.copy(
            step = SaleStep.Idle,
            selectedDishes = emptyList(),
            selectedSupplies = emptyList(),
            lastSaleSummary = null
        )
    }

    fun selectDish(dishId: String) {
        val dish = uiState.availableDishes.firstOrNull { it.id == dishId } ?: return
        if (uiState.selectedDishes.any { it.dish.id == dishId }) return

        uiState = uiState.copy(
            selectedDishes = uiState.selectedDishes + SaleDishSelection(
                id = UUID.randomUUID().toString(),
                dish = dish
            )
        )
    }

    fun increaseDishQuantity(selectionId: String) {
        uiState = uiState.copy(
            selectedDishes = uiState.selectedDishes.map { selection ->
                if (selection.id == selectionId) {
                    selection.copy(quantity = selection.quantity + 1)
                } else {
                    selection
                }
            }
        )
    }

    fun decreaseDishQuantity(selectionId: String) {
        uiState = uiState.copy(
            selectedDishes = uiState.selectedDishes.map { selection ->
                if (selection.id == selectionId) {
                    val newQuantity = (selection.quantity - 1).coerceAtLeast(1)
                    selection.copy(quantity = newQuantity)
                } else {
                    selection
                }
            }
        )
    }

    fun toggleDish(selectionId: String) {
        uiState = uiState.copy(
            selectedDishes = uiState.selectedDishes.map { selection ->
                if (selection.id == selectionId) {
                    selection.copy(isChecked = !selection.isChecked)
                } else {
                    selection
                }
            }
        )
    }

    fun removeDish(selectionId: String) {
        uiState = uiState.copy(
            selectedDishes = uiState.selectedDishes.filterNot { it.id == selectionId }
        )
    }

    fun proceedToSupplies() {
        if (uiState.selectedDishes.none { it.isChecked }) return
        uiState = uiState.copy(step = SaleStep.SelectSupplies)
    }

    fun backToDishes() {
        uiState = uiState.copy(step = SaleStep.SelectDishes)
    }

    fun selectSupply(supplyId: String) {
        val supply = uiState.availableSupplies.firstOrNull { it.id == supplyId } ?: return
        if (uiState.selectedSupplies.any { it.supply.id == supplyId }) return

        uiState = uiState.copy(
            selectedSupplies = uiState.selectedSupplies + SaleSupplySelection(
                id = UUID.randomUUID().toString(),
                supply = supply
            )
        )
    }

    fun updateSupplyQuantity(selectionId: String, quantityLabel: String) {
        uiState = uiState.copy(
            selectedSupplies = uiState.selectedSupplies.map { selection ->
                if (selection.id == selectionId) {
                    selection.copy(quantityLabel = quantityLabel)
                } else {
                    selection
                }
            }
        )
    }

    fun toggleSupply(selectionId: String) {
        uiState = uiState.copy(
            selectedSupplies = uiState.selectedSupplies.map { selection ->
                if (selection.id == selectionId) {
                    selection.copy(isChecked = !selection.isChecked)
                } else {
                    selection
                }
            }
        )
    }

    fun removeSupply(selectionId: String) {
        uiState = uiState.copy(
            selectedSupplies = uiState.selectedSupplies.filterNot { it.id == selectionId }
        )
    }

    fun saveSale() {
        val dishes = uiState.selectedDishes.filter { it.isChecked }
        val supplies = uiState.selectedSupplies.filter { it.isChecked }

        val summary = SaleSummary(
            dishes = dishes.map {
                SaleDishSummary(
                    name = it.dish.name,
                    unitPrice = it.dish.unitPrice,
                    quantity = it.quantity
                )
            },
            supplies = supplies.map {
                SaleSupplySummary(
                    name = it.supply.name,
                    quantity = it.quantityLabel
                )
            }
        )

        uiState = uiState.copy(
            step = SaleStep.Confirmation,
            lastSaleSummary = summary,
            totalRegisteredSales = uiState.totalRegisteredSales + 1
        )
    }

    fun closeSummary() {
        uiState = uiState.copy(
            step = SaleStep.Idle,
            selectedDishes = emptyList(),
            selectedSupplies = emptyList()
        )
    }
}
