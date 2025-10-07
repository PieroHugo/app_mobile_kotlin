package com.uitopic.restockmobile.features.monitoring.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.TextButton
import com.uitopic.restockmobile.ui.theme.RestockmobileTheme
import java.text.NumberFormat
import java.util.Locale

private enum class RegisterSaleStep {
    Intro,
    Dishes,
    Supplies,
    Success
}

private data class DishOption(
    val id: Int,
    val name: String,
    val unitPrice: Double
)

private data class DishSelection(
    val option: DishOption,
    val quantity: Int,
    val isSelected: Boolean
)

private data class SupplyOption(
    val id: Int,
    val name: String,
    val suggestedQuantity: String
)

private data class SupplySelection(
    val option: SupplyOption,
    val quantity: String,
    val isSelected: Boolean
)

private val peruvianCurrencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "PE"))

@Composable
fun RegisterSaleScreen(
    onBack: () -> Unit
) {
    val dishOptions = remember {
        listOf(
            DishOption(1, "Lomo Saltado", 20.50),
            DishOption(2, "Arroz con pollo", 15.50),
            DishOption(3, "Escabeche de pollo", 12.80),
            DishOption(4,"Tallarin con pollo con papa a la huancaina",18.00)
        )
    }
    val supplyOptions = remember {
        listOf(
            SupplyOption(1, "Huevo", "1"),
            SupplyOption(2, "Arroz", "250 g"),
            SupplyOption(3, "Inka cola personal", "1"),
            SupplyOption(4, "Papas", "150 g")
        )
    }

    var step by remember { mutableStateOf(RegisterSaleStep.Intro) }
    var dishSelections by remember { mutableStateOf<List<DishSelection>>(emptyList()) }
    var supplySelections by remember { mutableStateOf<List<SupplySelection>>(emptyList()) }

    fun resetSale() {
        step = RegisterSaleStep.Intro
        dishSelections = emptyList()
        supplySelections = emptyList()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MonitoringTopBar(onMenuClick = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                RegisterSaleStep.Intro -> {
                    RegisterSaleIntroCard(
                        onCreateSale = { step = RegisterSaleStep.Dishes }
                    )
                }

                RegisterSaleStep.Dishes -> {
                    RegisterSaleDishesCard(
                        dishOptions = dishOptions,
                        supplyOptions = supplyOptions,
                        selections = dishSelections,
                        onAddDish = { option ->
                            if (dishSelections.none { it.option.id == option.id }) {
                                dishSelections = dishSelections + DishSelection(option, quantity = 1, isSelected = true)
                            }
                        },
                        onAddSupply = { option ->
                            if (supplySelections.none { it.option.id == option.id }) {
                                supplySelections = supplySelections + SupplySelection(
                                    option = option,
                                    quantity = option.suggestedQuantity,
                                    isSelected = true
                                )
                            }
                        },
                        onToggleSelection = { selection, isSelected ->
                            dishSelections = dishSelections.map {
                                if (it.option.id == selection.option.id) it.copy(isSelected = isSelected) else it
                            }
                        },
                        onChangeQuantity = { selection, quantity ->
                            dishSelections = dishSelections.map {
                                if (it.option.id == selection.option.id) it.copy(quantity = quantity.coerceAtLeast(1)) else it
                            }
                        },
                        onRemoveSelection = { selection ->
                            dishSelections = dishSelections.filterNot { it.option.id == selection.option.id }
                        },
                        onCancel = { resetSale() },
                        onNext = {
                            if (dishSelections.isNotEmpty()) {
                                step = RegisterSaleStep.Supplies
                            }
                        }
                    )
                }

                RegisterSaleStep.Supplies -> {
                    RegisterSaleSuppliesCard(
                        supplyOptions = supplyOptions,
                        selections = supplySelections,
                        onAddSupply = { option ->
                            if (supplySelections.none { it.option.id == option.id }) {
                                supplySelections = supplySelections + SupplySelection(
                                    option = option,
                                    quantity = option.suggestedQuantity,
                                    isSelected = true
                                )
                            }
                        },
                        onToggleSelection = { selection, isSelected ->
                            supplySelections = supplySelections.map {
                                if (it.option.id == selection.option.id) it.copy(isSelected = isSelected) else it
                            }
                        },
                        onChangeQuantity = { selection, quantity ->
                            supplySelections = supplySelections.map {
                                if (it.option.id == selection.option.id) it.copy(quantity = quantity) else it
                            }
                        },
                        onRemoveSelection = { selection ->
                            supplySelections = supplySelections.filterNot { it.option.id == selection.option.id }
                        },
                        onBack = { step = RegisterSaleStep.Dishes },
                        onCancel = { resetSale() },
                        onSave = { step = RegisterSaleStep.Success }
                    )
                }

                RegisterSaleStep.Success -> {
                    RegisterSaleSuccessCard(
                        selectedDishes = dishSelections.filter { it.isSelected },
                        selectedSupplies = supplySelections.filter { it.isSelected },
                        onClose = { resetSale() }
                    )
                }
            }
        }
    }
}
@Composable
private fun MonitoringTopBar(
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlaceholderAvatar()
                Spacer(modifier = Modifier.width(12.dp))
                PlaceholderBrand(modifier = Modifier.weight(1f))
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceholderAvatar() {
    Surface(
        modifier = Modifier.size(48.dp),
        color = Color.White,
        shape = CircleShape,
        tonalElevation = 1.dp
    ) {}
}

@Composable
private fun PlaceholderBrand(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .height(16.dp),
            color = Color.White,
            shape = RoundedCornerShape(50)
        ) {}
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(12.dp),
            color = Color.White,
            shape = RoundedCornerShape(50)
        ) {}
    }
}
@Composable
private fun RegisterSaleIntroCard(
    onCreateSale: () -> Unit
) {
    RegisterSaleCard(
        title = "Register sale",
        subtitle = "You currently have no recorded sales. Enter your sales here to keep your inventory up to date.",
        topRightContent = {
            ActionButton(
                text = "NEW",
                icon = Icons.Outlined.Add,
                containerColor = Color(0xFF2E7D32),
                onClick = onCreateSale,
                modifier = Modifier.height(40.dp)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Restaurant,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
private fun RegisterSaleDishesCard(
    dishOptions: List<DishOption>,
    supplyOptions: List<SupplyOption>,
    selections: List<DishSelection>,
    onAddDish: (DishOption) -> Unit,
    onAddSupply: (SupplyOption) -> Unit,
    onToggleSelection: (DishSelection, Boolean) -> Unit,
    onChangeQuantity: (DishSelection, Int) -> Unit,
    onRemoveSelection: (DishSelection) -> Unit,
    onCancel: () -> Unit,
    onNext: () -> Unit
) {
    var selectedDish by remember { mutableStateOf<String?>(null) }
    var selectedSupply by remember { mutableStateOf<String?>(null) }

    RegisterSaleCard(
        title = "Register sale",
        subtitle = "Complete the details of a new sale to access the inventory update option."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SelectorField(
                label = "Dishes",
                placeholder = "Select a dish from your recipe list",
                options = dishOptions.map { it.name },
                selectedOption = selectedDish,
                onOptionSelected = { name ->
                    selectedDish = name
                    dishOptions.firstOrNull { it.name == name }?.let(onAddDish)
                }
            )

            SelectorField(
                label = "Additional supplies",
                placeholder = "Select a supply from your inventory",
                options = supplyOptions.map { it.name },
                selectedOption = selectedSupply,
                onOptionSelected = { name ->
                    selectedSupply = name
                    supplyOptions.firstOrNull { it.name == name }?.let(onAddSupply)
                }
            )

            if (selections.isEmpty()) {
                Text(
                    text = "Select the dishes and additional ingredients from the order to view the full sale.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                DishesTable(
                    selections = selections,
                    onToggleSelection = onToggleSelection,
                    onChangeQuantity = onChangeQuantity,
                    onRemoveSelection = onRemoveSelection
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    text = "CANCEL",
                    icon = Icons.Filled.Close,
                    containerColor = Color(0xFFD84343),
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "NEXT",
                    icon = Icons.Filled.KeyboardArrowRight,
                    containerColor = Color(0xFFFFA000),
                    onClick = onNext,
                    enabled = selections.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RegisterSaleSuppliesCard(
    supplyOptions: List<SupplyOption>,
    selections: List<SupplySelection>,
    onAddSupply: (SupplyOption) -> Unit,
    onToggleSelection: (SupplySelection, Boolean) -> Unit,
    onChangeQuantity: (SupplySelection, String) -> Unit,
    onRemoveSelection: (SupplySelection) -> Unit,
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    var selectedSupply by remember { mutableStateOf<String?>(null) }

    RegisterSaleCard(
        title = "Register sale",
        subtitle = "Complete the details of a new sale to access the inventory update option."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SelectorField(
                label = "Additional supplies",
                placeholder = "Select an input from your inventory",
                options = supplyOptions.map { it.name },
                selectedOption = selectedSupply,
                onOptionSelected = { name ->
                    selectedSupply = name
                    supplyOptions.firstOrNull { it.name == name }?.let(onAddSupply)
                }
            )

            if (selections.isEmpty()) {
                Text(
                    text = "No additional supplies were added for this sale.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                SuppliesTable(
                    selections = selections,
                    onToggleSelection = onToggleSelection,
                    onChangeQuantity = onChangeQuantity,
                    onRemoveSelection = onRemoveSelection
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    text = "BACK",
                    icon = Icons.Filled.KeyboardArrowLeft,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "CANCEL",
                    icon = Icons.Filled.Close,
                    containerColor = Color(0xFFD84343),
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "SAVE",
                    icon = Icons.Filled.Check,
                    containerColor = Color(0xFF2E7D32),
                    onClick = onSave,
                    enabled = selections.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
@Composable
private fun RegisterSaleSuccessCard(
    selectedDishes: List<DishSelection>,
    selectedSupplies: List<SupplySelection>,
    onClose: () -> Unit
) {
    RegisterSaleCard(
        title = "Sale successfully registered",
        subtitle = "The sale and its additional supplies have been saved correctly."
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(96.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            SummaryTable(
                title = "Dishes",
                headers = listOf("Name", "Unit price", "Quantity"),
                rows = selectedDishes.map { selection ->
                    listOf(
                        selection.option.name,
                        peruvianCurrencyFormatter.format(selection.option.unitPrice),
                        selection.quantity.toString()
                    )
                }
            )

            SummaryTable(
                title = "Additional supplies",
                headers = listOf("Nombre", "Cantidad"),
                rows = selectedSupplies.map { selection ->
                    listOf(
                        selection.option.name,
                        selection.quantity
                    )
                }
            )

            ActionButton(
                text = "CLOSE",
                icon = Icons.Filled.Close,
                containerColor = Color(0xFFD84343),
                onClick = onClose,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RegisterSaleCard(
    title: String,
    subtitle: String,
    topRightContent: (@Composable (() -> Unit))? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (topRightContent != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    topRightContent()
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorField(
    label: String,
    placeholder: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption ?: "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                singleLine = true,
                placeholder = { Text(placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
@Composable
private fun DishesTable(
    selections: List<DishSelection>,
    onToggleSelection: (DishSelection, Boolean) -> Unit,
    onChangeQuantity: (DishSelection, Int) -> Unit,
    onRemoveSelection: (DishSelection) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TableHeader(
            title = "Selected dishes",
            selected = selections.count { it.isSelected },
            total = selections.size
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 1.dp
        ) {
            Column {
                TableRow(
                    background = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    TableCell(text = "Name", weight = 0.4f, isHeader = true)
                    TableCell(text = "Unit price", weight = 0.3f, isHeader = true)
                    TableCell(text = "Quantity", weight = 0.2f, isHeader = true, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.width(8.dp))
                }

                selections.forEachIndexed { index, selection ->
                    TableRow {
                        TableCell(text = selection.option.name, weight = 0.4f)
                        TableCell(
                            text = peruvianCurrencyFormatter.format(selection.option.unitPrice),
                            weight = 0.3f
                        )
                        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
                            QuantitySelector(
                                quantity = selection.quantity,
                                onDecrease = { onChangeQuantity(selection, selection.quantity - 1) },
                                onIncrease = { onChangeQuantity(selection, selection.quantity + 1) }
                            )
                        }
                        Checkbox(
                            checked = selection.isSelected,
                            onCheckedChange = { onToggleSelection(selection, it) }
                        )
                    }

                    if (index < selections.lastIndex) {
                        Divider()
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    selections.lastOrNull()?.let { last -> onRemoveSelection(last) }
                },
                enabled = selections.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.error,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                )
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Delete")
            }
        }
    }
}

@Composable
private fun SuppliesTable(
    selections: List<SupplySelection>,
    onToggleSelection: (SupplySelection, Boolean) -> Unit,
    onChangeQuantity: (SupplySelection, String) -> Unit,
    onRemoveSelection: (SupplySelection) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TableHeader(
            title = "Selected additional supplies",
            selected = selections.count { it.isSelected },
            total = selections.size
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 1.dp
        ) {
            Column {
                TableRow(
                    background = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    TableCell(text = "Name", weight = 0.5f, isHeader = true)
                    TableCell(text = "Quantity", weight = 0.3f, isHeader = true)
                    Spacer(modifier = Modifier.width(8.dp))
                }

                selections.forEachIndexed { index, selection ->
                    TableRow {
                        TableCell(text = selection.option.name, weight = 0.5f)
                        Box(modifier = Modifier.weight(0.3f)) {
                            OutlinedTextField(
                                value = selection.quantity,
                                onValueChange = { onChangeQuantity(selection, it) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default
                            )
                        }
                        Checkbox(
                            checked = selection.isSelected,
                            onCheckedChange = { onToggleSelection(selection, it) }
                        )
                    }

                    if (index < selections.lastIndex) {
                        Divider()
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = { selections.lastOrNull()?.let(onRemoveSelection) },
                enabled = selections.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.error,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                )
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Delete")
            }
        }
    }
}

@Composable
private fun TableHeader(
    title: String,
    selected: Int,
    total: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = null)
            }
            Text(
                text = "$selected of $total",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            IconButton(onClick = { }) {
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}

@Composable
private fun TableRow(
    background: Color = Color.Transparent,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = if (isHeader) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyMedium,
        fontWeight = if (isHeader) FontWeight.SemiBold else FontWeight.Normal,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { if (quantity > 1) onDecrease() }, enabled = quantity > 1) {
            Icon(Icons.Outlined.Remove, contentDescription = null)
        }
        Text(
            text = quantity.toString(),
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = onIncrease) {
            Icon(Icons.Outlined.Add, contentDescription = null)
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector? = null,
    containerColor: Color,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.6f)
        )
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SummaryTable(
    title: String,
    headers: List<String>,
    rows: List<List<String>>
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 1.dp
        ) {
            Column {
                TableRow(
                    background = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    headers.forEach { header ->
                        TableCell(text = header, weight = 1f, isHeader = true)
                    }
                }
                if (rows.isEmpty()) {
                    TableRow {
                        Text(
                            text = "No records available",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    rows.forEachIndexed { index, values ->
                        TableRow {
                            values.forEach { value ->
                                TableCell(text = value, weight = 1f)
                            }
                        }
                        if (index < rows.lastIndex) {
                            Divider()
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun RegisterSaleIntroPreview() {
    RestockmobileTheme {
        RegisterSaleIntroCard(onCreateSale = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterSaleDishesPreview() {
    RestockmobileTheme {
        RegisterSaleDishesCard(
            dishOptions = listOf(
                DishOption(1, "Lomo Saltado", 20.50),
                DishOption(2, "Arroz con Pollo", 15.50)
            ),
            supplyOptions = listOf(
                SupplyOption(1, "Huevo", "1"),
                SupplyOption(2, "Arroz", "250 g")
            ),
            selections = listOf(
                DishSelection(DishOption(1, "Lomo Saltado", 20.50), 1, true),
                DishSelection(DishOption(2, "Arroz con Pollo", 15.50), 2, true)
            ),
            onAddDish = {},
            onAddSupply = {},
            onToggleSelection = { _, _ -> },
            onChangeQuantity = { _, _ -> },
            onRemoveSelection = {},
            onCancel = {},
            onNext = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterSaleSuppliesPreview() {
    RestockmobileTheme {
        RegisterSaleSuppliesCard(
            supplyOptions = listOf(
                SupplyOption(1, "Huevo", "1"),
                SupplyOption(2, "Arroz", "250 g")
            ),
            selections = listOf(
                SupplySelection(SupplyOption(1, "Huevo", "1"), "1", true),
                SupplySelection(SupplyOption(2, "Arroz", "250 g"), "250 g", true)
            ),
            onAddSupply = {},
            onToggleSelection = { _, _ -> },
            onChangeQuantity = { _, _ -> },
            onRemoveSelection = {},
            onBack = {},
            onCancel = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterSaleSuccessPreview() {
    RestockmobileTheme {
        RegisterSaleSuccessCard(
            selectedDishes = listOf(
                DishSelection(DishOption(1, "Lomo Saltado", 20.50), 1, true),
                DishSelection(DishOption(2, "Arroz con Pollo", 15.50), 1, true)
            ),
            selectedSupplies = listOf(
                SupplySelection(SupplyOption(1, "Huevo", "1"), "1", true),
                SupplySelection(SupplyOption(2, "Arroz", "250 g"), "250 g", true)
            ),
            onClose = {}
        )
    }
}
