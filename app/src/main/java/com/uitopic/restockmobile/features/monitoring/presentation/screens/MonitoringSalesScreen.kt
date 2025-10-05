package com.uitopic.restockmobile.features.monitoring.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uitopic.restockmobile.features.monitoring.domain.model.SaleSummary
import com.uitopic.restockmobile.features.monitoring.presentation.state.SaleDishSelection
import com.uitopic.restockmobile.features.monitoring.presentation.state.SaleStep
import com.uitopic.restockmobile.features.monitoring.presentation.state.SaleSupplySelection
import com.uitopic.restockmobile.features.monitoring.presentation.viewmodel.SalesViewModel
import java.util.Locale

private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF0F1A23), Color(0xFF15232D))
)
private val CardBackground = Color(0xFFFDFDFD)
private val AccentGreen = Color(0xFF2ECC71)
private val AccentRed = Color(0xFFE74C3C)
private val AccentOrange = Color(0xFFF39C12)
private val AccentYellow = Color(0xFFF1C40F)
private val DeepBlue = Color(0xFF0B141B)

@Composable
fun MonitoringSalesScreen(
    onNavigateBack: () -> Unit,
    viewModel: SalesViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    Surface(color = DeepBlue, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGradient)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MonitoringHeader(onNavigateBack = onNavigateBack)

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp, vertical = 32.dp)
                    ) {
                        when (state.step) {
                            SaleStep.Idle -> IdleStateContent(
                                totalSales = state.totalRegisteredSales,
                                onStart = { viewModel.startNewSale() }
                            )

                            SaleStep.SelectDishes -> DishSelectionContent(
                                selections = state.selectedDishes,
                                availableOptions = state.availableDishes.map { it.name to it.id },
                                onSelectDish = viewModel::selectDish,
                                onIncrease = viewModel::increaseDishQuantity,
                                onDecrease = viewModel::decreaseDishQuantity,
                                onToggle = viewModel::toggleDish,
                                onRemove = viewModel::removeDish,
                                onCancel = { viewModel.cancelSale() },
                                onNext = { viewModel.proceedToSupplies() }
                            )

                            SaleStep.SelectSupplies -> SupplySelectionContent(
                                selections = state.selectedSupplies,
                                availableOptions = state.availableSupplies.map { it.name to it.id },
                                onSelectSupply = viewModel::selectSupply,
                                onQuantityChanged = viewModel::updateSupplyQuantity,
                                onToggle = viewModel::toggleSupply,
                                onRemove = viewModel::removeSupply,
                                onBack = { viewModel.backToDishes() },
                                onCancel = { viewModel.cancelSale() },
                                onSave = { viewModel.saveSale() }
                            )

                            SaleStep.Confirmation -> state.lastSaleSummary?.let { summary ->
                                SaleSummaryContent(
                                    summary = summary,
                                    onClose = { viewModel.closeSummary() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonitoringHeader(onNavigateBack: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "R",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Restock",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        Box {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = AccentGreen
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Return to home") },
                    onClick = {
                        menuExpanded = false
                        onNavigateBack()
                    }
                )
            }
        }
    }
}

@Composable
private fun IdleStateContent(totalSales: Int, onStart: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Register sale",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = DeepBlue
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = buildString {
                append("You currently have ")
                when (totalSales) {
                    0 -> append("no recorded sales.")
                    1 -> append("1 recorded sale.")
                    else -> append("$totalSales recorded sales.")
                }
                append(" Enter your sales here to keep your inventory up to date.")
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F7A85)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                tint = Color(0xFFB5BDC4),
                modifier = Modifier.size(72.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ActionButton(
                text = "NEW",
                iconTint = Color.White,
                icon = Icons.Default.Add,
                backgroundColor = AccentGreen,
                onClick = onStart
            )
        }
    }
}

@Composable
private fun DishSelectionContent(
    selections: List<SaleDishSelection>,
    availableOptions: List<Pair<String, String>>,
    onSelectDish: (String) -> Unit,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit,
    onCancel: () -> Unit,
    onNext: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Register sale",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = DeepBlue
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Complete the details of a new sale to access the inventory update option.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F7A85)
        )

        Spacer(modifier = Modifier.height(24.dp))

        DropdownPicker(
            label = "Dishes",
            placeholder = "Select a dish from your recipe list",
            options = availableOptions,
            onOptionSelected = onSelectDish
        )

        Spacer(modifier = Modifier.height(24.dp))

        SelectedDishesList(
            selections = selections,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
            onToggle = onToggle,
            onRemove = onRemove
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButton(
                text = "CANCEL",
                icon = Icons.Default.Close,
                iconTint = Color.White,
                backgroundColor = AccentRed,
                onClick = onCancel
            )

            ActionButton(
                text = "NEXT",
                icon = Icons.Default.Check,
                iconTint = Color.White,
                backgroundColor = AccentOrange,
                onClick = onNext,
                enabled = selections.any { it.isChecked }
            )
        }
    }
}

@Composable
private fun SupplySelectionContent(
    selections: List<SaleSupplySelection>,
    availableOptions: List<Pair<String, String>>,
    onSelectSupply: (String) -> Unit,
    onQuantityChanged: (String, String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit,
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Register sale",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = DeepBlue
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Complete the details of a new sale to access the inventory update option.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F7A85)
        )

        Spacer(modifier = Modifier.height(24.dp))

        DropdownPicker(
            label = "Additional supplies",
            placeholder = "Select a supply from your inventory",
            options = availableOptions,
            onOptionSelected = onSelectSupply
        )

        Spacer(modifier = Modifier.height(24.dp))

        SelectedSuppliesList(
            selections = selections,
            onQuantityChanged = onQuantityChanged,
            onToggle = onToggle,
            onRemove = onRemove
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionButton(
                    text = "BACK",
                    icon = Icons.Default.ArrowBack,
                    iconTint = DeepBlue,
                    backgroundColor = AccentYellow,
                    onClick = onBack,
                    contentColor = DeepBlue
                )

                ActionButton(
                    text = "CANCEL",
                    icon = Icons.Default.Close,
                    iconTint = Color.White,
                    backgroundColor = AccentRed,
                    onClick = onCancel
                )
            }

            ActionButton(
                text = "SAVE",
                icon = Icons.Default.Check,
                iconTint = Color.White,
                backgroundColor = AccentGreen,
                onClick = onSave,
                enabled = selections.any { it.isChecked }
            )
        }
    }
}

@Composable
private fun SaleSummaryContent(
    summary: SaleSummary,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = AccentGreen,
                modifier = Modifier.size(72.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SALE SUCCESSFULLY REGISTERED",
            style = MaterialTheme.typography.titleMedium,
            color = DeepBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        SummaryList(
            title = "Dishes",
            rows = summary.dishes.map {
                listOf(
                    it.name,
                    formatCurrency(it.unitPrice),
                    it.quantity.toString()
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SummaryList(
            title = "additional supplies",
            rows = summary.supplies.map { listOf(it.name, it.quantity) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ActionButton(
                text = "CLOSE",
                icon = Icons.Default.Close,
                iconTint = Color.White,
                backgroundColor = AccentRed,
                onClick = onClose
            )
        }
    }
}

@Composable
private fun SelectedDishesList(
    selections: List<SaleDishSelection>,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    if (selections.isEmpty()) {
        InfoMessage(text = "Select the dishes and additional ingredients from the order to view the full sale.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F7F9), RoundedCornerShape(18.dp))
            .padding(20.dp)
    ) {
        val checkedCount = selections.count { it.isChecked }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Selected dishes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DeepBlue
            )
            Text(
                text = "$checkedCount of ${selections.size}",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF6F7A85)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(selections, key = { _, item -> item.id }) { _, item ->
                DishRow(
                    selection = item,
                    onIncrease = onIncrease,
                    onDecrease = onDecrease,
                    onToggle = onToggle,
                    onRemove = onRemove
                )
            }
        }
    }
}

@Composable
private fun SelectedSuppliesList(
    selections: List<SaleSupplySelection>,
    onQuantityChanged: (String, String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    if (selections.isEmpty()) {
        InfoMessage(text = "Select the dishes and additional ingredients from the order to view the full sale.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F7F9), RoundedCornerShape(18.dp))
            .padding(20.dp)
    ) {
        val checkedCount = selections.count { it.isChecked }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Selected additional supplies",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DeepBlue
            )
            Text(
                text = "$checkedCount of ${selections.size}",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF6F7A85)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(selections, key = { _, item -> item.id }) { _, item ->
                SupplyRow(
                    selection = item,
                    onQuantityChanged = onQuantityChanged,
                    onToggle = onToggle,
                    onRemove = onRemove
                )
            }
        }
    }
}

@Composable
private fun DishRow(
    selection: SaleDishSelection,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = selection.dish.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = DeepBlue
            )
            Text(
                text = formatCurrency(selection.dish.unitPrice),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6F7A85)
            )
        }

        QuantityStepper(
            quantity = selection.quantity,
            onIncrease = { onIncrease(selection.id) },
            onDecrease = { onDecrease(selection.id) }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Checkbox(
            checked = selection.isChecked,
            onCheckedChange = { onToggle(selection.id) }
        )

        IconButton(onClick = { onRemove(selection.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFF6F7A85)
            )
        }
    }
}

@Composable
private fun SupplyRow(
    selection: SaleSupplySelection,
    onQuantityChanged: (String, String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = selection.supply.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = DeepBlue
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        OutlinedTextField(
            value = selection.quantityLabel,
            onValueChange = { onQuantityChanged(selection.id, it) },
            modifier = Modifier.width(120.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = AccentGreen,
                cursorColor = DeepBlue,
                focusedLabelColor = DeepBlue
            ),
            label = { Text("Quantity") }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Checkbox(
            checked = selection.isChecked,
            onCheckedChange = { onToggle(selection.id) }
        )

        IconButton(onClick = { onRemove(selection.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFF6F7A85)
            )
        }
    }
}

@Composable
private fun QuantityStepper(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDecrease) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = DeepBlue)
        }
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.Center,
            color = DeepBlue
        )
        IconButton(onClick = onIncrease) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = DeepBlue)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownPicker(
    label: String,
    placeholder: String,
    options: List<Pair<String, String>>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DeepBlue
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = if (selectedLabel.isEmpty()) "" else selectedLabel,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                placeholder = { Text(placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = AccentGreen,
                    cursorColor = DeepBlue,
                    focusedLabelColor = DeepBlue
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.first) },
                        onClick = {
                            selectedLabel = option.first
                            expanded = false
                            onOptionSelected(option.second)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoMessage(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F7F9), RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F7A85),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SummaryList(title: String, rows: List<List<String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F7F9), RoundedCornerShape(18.dp))
            .padding(20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DeepBlue
        )

        Spacer(modifier = Modifier.height(16.dp))

        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEachIndexed { position, cell ->
                    Text(
                        text = cell,
                        style = if (position == 0) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
                        color = DeepBlue,
                        modifier = Modifier.weight(if (position == 0) 1.4f else 1f)
                    )
                }
            }
            if (index != rows.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color(0xFFE0E6EB))
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor, contentColor = contentColor),
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

private fun formatCurrency(value: Double): String {
    return "S/ %.2f".format(Locale.US, value)
}
