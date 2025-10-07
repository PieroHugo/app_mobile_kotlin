package com.uitopic.restockmobile.features.monitoring.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uitopic.restockmobile.features.monitoring.domain.models.DishSelection
import com.uitopic.restockmobile.features.monitoring.domain.models.SaleRecord
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesDish
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesSupply
import com.uitopic.restockmobile.features.monitoring.domain.models.SupplySelection
import com.uitopic.restockmobile.features.monitoring.presentation.components.DishSelectionTable
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringBackground
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringBodyStyle
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringButtonRow
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringCardBackground
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringDropdown
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringDropdownOption
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringFilledButton
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringOutlinedButton
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringGreen
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringLabelStyle
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringOrange
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringPrimaryText
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringRed
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringSecondaryText
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringTitleStyle
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringSuccessSummary
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringTableCellStyle
import com.uitopic.restockmobile.features.monitoring.presentation.components.MonitoringTopBar
import com.uitopic.restockmobile.features.monitoring.presentation.components.SupplySelectionTable
import com.uitopic.restockmobile.features.monitoring.presentation.states.MonitoringUiState
import com.uitopic.restockmobile.features.monitoring.presentation.states.SalesStep
import com.uitopic.restockmobile.features.monitoring.presentation.viewmodels.MonitoringViewModel

@Composable
fun SalesMonitoringRoute(
    viewModel: MonitoringViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    SalesMonitoringScreen(
        state = state,
        onStartNewSale = viewModel::startNewSale,
        onCancelSale = viewModel::cancelSale,
        onDishSelected = viewModel::selectDish,
        onDishQuantityChange = viewModel::updateDishQuantity,
        onDishToggle = viewModel::toggleDishConfirmation,
        onDishRemoved = viewModel::removeDish,
        onNextStep = viewModel::goToNextStep,
        onPreviousStep = viewModel::goToPreviousStep,
        onSupplySelected = viewModel::selectSupply,
        onSupplyQuantityChange = viewModel::updateSupplyQuantity,
        onSupplyToggle = viewModel::toggleSupplyConfirmation,
        onSupplyRemoved = viewModel::removeSupply,
        onRegisterSale = viewModel::registerSale,
        onCloseSummary = viewModel::closeSummary
    )
}

@Composable
fun SalesMonitoringScreen(
    state: MonitoringUiState,
    onStartNewSale: () -> Unit,
    onCancelSale: () -> Unit,
    onDishSelected: (String) -> Unit,
    onDishQuantityChange: (String, Int) -> Unit,
    onDishToggle: (String) -> Unit,
    onDishRemoved: (String) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onSupplySelected: (String) -> Unit,
    onSupplyQuantityChange: (String, String) -> Unit,
    onSupplyToggle: (String) -> Unit,
    onSupplyRemoved: (String) -> Unit,
    onRegisterSale: () -> Unit,
    onCloseSummary: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MonitoringBackground)
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(32.dp),
            color = MonitoringCardBackground,
            tonalElevation = 0.dp,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                MonitoringTopBar()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Register sale",
                    style = MonitoringTitleStyle
                )

                Text(
                    text = "Complete the details of a new sale to access the inventory update option.",
                    style = MonitoringBodyStyle,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                when (state.currentStep) {
                    SalesStep.EMPTY -> SalesEmptyContent(onStartNewSale)

                    SalesStep.DISHES -> SalesDishSelectionContent(
                        state = state,
                        onDishSelected = onDishSelected,
                        onDishQuantityChange = onDishQuantityChange,
                        onDishToggle = onDishToggle,
                        onDishRemoved = onDishRemoved,
                        onCancelSale = onCancelSale,
                        onNextStep = onNextStep
                    )

                    SalesStep.SUPPLIES -> SalesSupplySelectionContent(
                        state = state,
                        onSupplySelected = onSupplySelected,
                        onSupplyQuantityChange = onSupplyQuantityChange,
                        onSupplyToggle = onSupplyToggle,
                        onSupplyRemoved = onSupplyRemoved,
                        onCancelSale = onCancelSale,
                        onPreviousStep = onPreviousStep,
                        onRegisterSale = onRegisterSale
                    )

                    SalesStep.SUCCESS -> SalesSuccessContent(
                        record = state.lastRegisteredSale,
                        onCloseSummary = onCloseSummary
                    )
                }
            }
        }
    }
}

@Composable
private fun SalesEmptyContent(onStartNewSale: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You currently have no recorded sales. Enter your sales here to keep your inventory up to date.",
            style = MonitoringBodyStyle,
            color = MonitoringSecondaryText,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        MonitoringFilledButton(
            text = "NEW",
            onClick = onStartNewSale,
            containerColor = MonitoringGreen,
            leadingIcon = Icons.Default.Add
        )
    }
}

@Composable
private fun SalesDishSelectionContent(
    state: MonitoringUiState,
    onDishSelected: (String) -> Unit,
    onDishQuantityChange: (String, Int) -> Unit,
    onDishToggle: (String) -> Unit,
    onDishRemoved: (String) -> Unit,
    onCancelSale: () -> Unit,
    onNextStep: () -> Unit
) {
    val dishOptions = state.availableDishes.map { MonitoringDropdownOption(it.id, it.name) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        MonitoringDropdown(
            label = "Dishes",
            placeholder = "Select a dish from your recipe list",
            options = dishOptions,
            selectedOptionId = state.selectedDishId,
            onOptionSelected = onDishSelected
        )

        Text(
            text = "Select the dishes and additional ingredients from the order to view the full sale.",
            style = MonitoringBodyStyle,
            color = MonitoringSecondaryText
        )

        Text(
            text = "Selected dishes",
            style = MonitoringLabelStyle
        )

        DishSelectionTable(
            selections = state.selectedDishes,
            onQuantityChange = onDishQuantityChange,
            onToggle = onDishToggle,
            onRemove = onDishRemoved
        )

        val confirmed = state.selectedDishes.count { it.isConfirmed }
        val total = state.selectedDishes.size
        if (total > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "$confirmed of $total",
                    style = MonitoringTableCellStyle
                )
            }
        }

        MonitoringButtonRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            MonitoringFilledButton(
                text = "CANCEL",
                onClick = onCancelSale,
                containerColor = MonitoringRed,
                leadingIcon = Icons.Default.Close
            )

            MonitoringFilledButton(
                text = "NEXT",
                onClick = onNextStep,
                containerColor = MonitoringOrange,
                leadingIcon = Icons.Default.ChevronRight,
                enabled = state.selectedDishes.any { it.isConfirmed }
            )
        }
    }
}

@Composable
private fun SalesSupplySelectionContent(
    state: MonitoringUiState,
    onSupplySelected: (String) -> Unit,
    onSupplyQuantityChange: (String, String) -> Unit,
    onSupplyToggle: (String) -> Unit,
    onSupplyRemoved: (String) -> Unit,
    onCancelSale: () -> Unit,
    onPreviousStep: () -> Unit,
    onRegisterSale: () -> Unit
) {
    val supplyOptions = state.availableSupplies.map { MonitoringDropdownOption(it.id, it.name) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        MonitoringDropdown(
            label = "Additional supplies",
            placeholder = "Select a supply from your inventory",
            options = supplyOptions,
            selectedOptionId = state.selectedSupplyId,
            onOptionSelected = onSupplySelected
        )

        Text(
            text = "Select the dishes and additional ingredients from the order to view the full sale.",
            style = MonitoringBodyStyle,
            color = MonitoringSecondaryText
        )

        Text(
            text = "Selected additional supplies",
            style = MonitoringLabelStyle
        )

        SupplySelectionTable(
            selections = state.selectedSupplies,
            onQuantityChange = onSupplyQuantityChange,
            onToggle = onSupplyToggle,
            onRemove = onSupplyRemoved
        )

        val confirmed = state.selectedSupplies.count { it.isConfirmed }
        val total = state.selectedSupplies.size
        if (total > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "$confirmed of $total",
                    style = MonitoringTableCellStyle
                )
            }
        }

        MonitoringButtonRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            MonitoringOutlinedButton(
                text = "BACK",
                onClick = onPreviousStep,
                leadingIcon = Icons.Default.ArrowBack,
                contentColor = MonitoringPrimaryText
            )

            MonitoringFilledButton(
                text = "CANCEL",
                onClick = onCancelSale,
                containerColor = MonitoringRed,
                leadingIcon = Icons.Default.Close
            )

            MonitoringFilledButton(
                text = "SAVE",
                onClick = onRegisterSale,
                containerColor = MonitoringGreen,
                leadingIcon = Icons.Default.Check
            )
        }
    }
}

@Composable
private fun SalesSuccessContent(
    record: SaleRecord?,
    onCloseSummary: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        record?.let {
            MonitoringSuccessSummary(record = it)
        }

        MonitoringFilledButton(
            text = "CLOSE",
            onClick = onCloseSummary,
            containerColor = MonitoringRed,
            leadingIcon = Icons.Default.Close
        )
    }
}

@Preview(showBackground = true)
private fun SalesEmptyPreview() {
    SalesMonitoringScreen(
        state = MonitoringUiState(),
        onStartNewSale = {},
        onCancelSale = {},
        onDishSelected = {},
        onDishQuantityChange = { _, _ -> },
        onDishToggle = {},
        onDishRemoved = {},
        onNextStep = {},
        onPreviousStep = {},
        onSupplySelected = {},
        onSupplyQuantityChange = { _, _ -> },
        onSupplyToggle = {},
        onSupplyRemoved = {},
        onRegisterSale = {},
        onCloseSummary = {}
    )
}

@Preview(showBackground = true)
private fun SalesDishesPreview() {
    SalesMonitoringScreen(
        state = MonitoringUiState(
            currentStep = SalesStep.DISHES,
            availableDishes = sampleDishes(),
            selectedDishes = sampleDishSelections()
        ),
        onStartNewSale = {},
        onCancelSale = {},
        onDishSelected = {},
        onDishQuantityChange = { _, _ -> },
        onDishToggle = {},
        onDishRemoved = {},
        onNextStep = {},
        onPreviousStep = {},
        onSupplySelected = {},
        onSupplyQuantityChange = { _, _ -> },
        onSupplyToggle = {},
        onSupplyRemoved = {},
        onRegisterSale = {},
        onCloseSummary = {}
    )
}

@Preview(showBackground = true)
private fun SalesSuppliesPreview() {
    SalesMonitoringScreen(
        state = MonitoringUiState(
            currentStep = SalesStep.SUPPLIES,
            availableSupplies = sampleSupplies(),
            selectedSupplies = sampleSupplySelections()
        ),
        onStartNewSale = {},
        onCancelSale = {},
        onDishSelected = {},
        onDishQuantityChange = { _, _ -> },
        onDishToggle = {},
        onDishRemoved = {},
        onNextStep = {},
        onPreviousStep = {},
        onSupplySelected = {},
        onSupplyQuantityChange = { _, _ -> },
        onSupplyToggle = {},
        onSupplyRemoved = {},
        onRegisterSale = {},
        onCloseSummary = {}
    )
}

@Preview(showBackground = true)
private fun SalesSuccessPreview() {
    SalesMonitoringScreen(
        state = MonitoringUiState(
            currentStep = SalesStep.SUCCESS,
            lastRegisteredSale = SaleRecord(
                dishes = sampleDishSelections(),
                supplies = sampleSupplySelections()
            )
        ),
        onStartNewSale = {},
        onCancelSale = {},
        onDishSelected = {},
        onDishQuantityChange = { _, _ -> },
        onDishToggle = {},
        onDishRemoved = {},
        onNextStep = {},
        onPreviousStep = {},
        onSupplySelected = {},
        onSupplyQuantityChange = { _, _ -> },
        onSupplyToggle = {},
        onSupplyRemoved = {},
        onRegisterSale = {},
        onCloseSummary = {}
    )
}

private fun sampleDishes(): List<SalesDish> = listOf(
    SalesDish(id = "1", name = "Lomo Saltado", unitPrice = 20.50),
    SalesDish(id = "2", name = "Arroz con Pollo", unitPrice = 21.50),
    SalesDish(id = "3", name = "Sopa dieta", unitPrice = 15.50)
)

private fun sampleSupplies(): List<SalesSupply> = listOf(
    SalesSupply(id = "1", name = "Huevo"),
    SalesSupply(id = "2", name = "Arroz", measurementHint = "gramos"),
    SalesSupply(id = "3", name = "Inka cola personal", measurementHint = "botellas")
)

private fun sampleDishSelections(): List<DishSelection> = listOf(
    DishSelection(sampleDishes()[0], 1, true),
    DishSelection(sampleDishes()[1], 1, true),
    DishSelection(sampleDishes()[2], 1, false)
)

private fun sampleSupplySelections(): List<SupplySelection> = listOf(
    SupplySelection(sampleSupplies()[0], "1", true),
    SupplySelection(sampleSupplies()[1], "250", true)
)

