package com.uitopic.restockmobile.features.monitoring.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uitopic.restockmobile.features.monitoring.domain.models.DishSelection
import com.uitopic.restockmobile.features.monitoring.domain.models.SaleRecord
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesDish
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesSupply
import com.uitopic.restockmobile.features.monitoring.domain.models.SupplySelection
import com.uitopic.restockmobile.features.monitoring.domain.usecases.GetAvailableDishesUseCase
import com.uitopic.restockmobile.features.monitoring.domain.usecases.GetAvailableSuppliesUseCase
import com.uitopic.restockmobile.features.monitoring.domain.usecases.RegisterSaleUseCase
import com.uitopic.restockmobile.features.monitoring.presentation.states.MonitoringUiState
import com.uitopic.restockmobile.features.monitoring.presentation.states.SalesStep
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
    getAvailableDishesUseCase: GetAvailableDishesUseCase,
    getAvailableSuppliesUseCase: GetAvailableSuppliesUseCase,
    private val registerSaleUseCase: RegisterSaleUseCase
) : ViewModel() {

    var uiState by mutableStateOf(MonitoringUiState())
        private set

    init {
        val dishes = getAvailableDishesUseCase()
        val supplies = getAvailableSuppliesUseCase()
        uiState = uiState.copy(
            availableDishes = dishes,
            availableSupplies = supplies
        )
    }

    fun startNewSale() {
        uiState = uiState.copy(
            currentStep = SalesStep.DISHES,
            selectedDishes = emptyList(),
            selectedSupplies = emptyList(),
            selectedDishId = null,
            selectedSupplyId = null,
            lastRegisteredSale = null
        )
    }

    fun cancelSale() {
        uiState = uiState.copy(
            currentStep = SalesStep.EMPTY,
            selectedDishes = emptyList(),
            selectedSupplies = emptyList(),
            selectedDishId = null,
            selectedSupplyId = null,
            lastRegisteredSale = null
        )
    }

    fun selectDish(dishId: String) {
        val dish = uiState.availableDishes.find { it.id == dishId } ?: return
        if (uiState.selectedDishes.any { it.dish.id == dishId }) {
            uiState = uiState.copy(selectedDishId = dishId)
            return
        }

        val newSelection = uiState.selectedDishes + DishSelection(
            dish = dish,
            quantity = 1,
            isConfirmed = true
        )

        uiState = uiState.copy(
            selectedDishes = newSelection,
            selectedDishId = dishId
        )
    }

    fun updateDishQuantity(dishId: String, quantity: Int) {
        if (quantity <= 0) return
        val updated = uiState.selectedDishes.map {
            if (it.dish.id == dishId) it.copy(quantity = quantity) else it
        }
        uiState = uiState.copy(selectedDishes = updated)
    }

    fun toggleDishConfirmation(dishId: String) {
        val updated = uiState.selectedDishes.map {
            if (it.dish.id == dishId) it.copy(isConfirmed = !it.isConfirmed) else it
        }
        uiState = uiState.copy(selectedDishes = updated)
    }

    fun removeDish(dishId: String) {
        val updated = uiState.selectedDishes.filterNot { it.dish.id == dishId }
        uiState = uiState.copy(
            selectedDishes = updated,
            selectedDishId = updated.lastOrNull()?.dish?.id
        )
    }

    fun selectSupply(supplyId: String) {
        val supply = uiState.availableSupplies.find { it.id == supplyId } ?: return
        if (uiState.selectedSupplies.any { it.supply.id == supplyId }) {
            uiState = uiState.copy(selectedSupplyId = supplyId)
            return
        }

        val defaultQuantity = when (supply.measurementHint) {
            "gramos" -> "250"
            "botellas" -> "1"
            else -> "1"
        }

        val newSelection = uiState.selectedSupplies + SupplySelection(
            supply = supply,
            quantity = defaultQuantity,
            isConfirmed = true
        )

        uiState = uiState.copy(
            selectedSupplies = newSelection,
            selectedSupplyId = supplyId
        )
    }

    fun updateSupplyQuantity(supplyId: String, quantity: String) {
        if (quantity.isBlank()) return
        val updated = uiState.selectedSupplies.map {
            if (it.supply.id == supplyId) it.copy(quantity = quantity) else it
        }
        uiState = uiState.copy(selectedSupplies = updated)
    }

    fun toggleSupplyConfirmation(supplyId: String) {
        val updated = uiState.selectedSupplies.map {
            if (it.supply.id == supplyId) it.copy(isConfirmed = !it.isConfirmed) else it
        }
        uiState = uiState.copy(selectedSupplies = updated)
    }

    fun removeSupply(supplyId: String) {
        val updated = uiState.selectedSupplies.filterNot { it.supply.id == supplyId }
        uiState = uiState.copy(
            selectedSupplies = updated,
            selectedSupplyId = updated.lastOrNull()?.supply?.id
        )
    }

    fun goToNextStep() {
        when (uiState.currentStep) {
            SalesStep.DISHES -> if (uiState.hasSelectedDishes) {
                uiState = uiState.copy(currentStep = SalesStep.SUPPLIES)
            }

            SalesStep.EMPTY,
            SalesStep.SUPPLIES,
            SalesStep.SUCCESS -> Unit
        }
    }

    fun goToPreviousStep() {
        if (uiState.currentStep == SalesStep.SUPPLIES) {
            uiState = uiState.copy(currentStep = SalesStep.DISHES)
        }
    }

    fun registerSale() {
        val confirmedDishes = uiState.selectedDishes.filter { it.isConfirmed }
        if (confirmedDishes.isEmpty()) return
        val confirmedSupplies = uiState.selectedSupplies.filter { it.isConfirmed }
        val saleRecord = registerSaleUseCase(
            SaleRecord(
                dishes = confirmedDishes,
                supplies = confirmedSupplies
            )
        )

        uiState = uiState.copy(
            currentStep = SalesStep.SUCCESS,
            lastRegisteredSale = saleRecord
        )
    }

    fun closeSummary() {
        uiState = uiState.copy(
            currentStep = SalesStep.EMPTY,
            selectedDishes = emptyList(),
            selectedSupplies = emptyList(),
            selectedDishId = null,
            selectedSupplyId = null,
            lastRegisteredSale = null
        )
    }
}

