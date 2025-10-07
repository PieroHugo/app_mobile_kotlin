package com.uitopic.restockmobile.features.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uitopic.restockmobile.features.monitoring.domain.models.DishSelection
import com.uitopic.restockmobile.features.monitoring.domain.models.SupplySelection

@Composable
fun DishSelectionTable(
    selections: List<DishSelection>,
    onQuantityChange: (String, Int) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MonitoringSelectionTable(
        modifier = modifier,
        header = listOf("Name", "Unit price", "Quantity"),
        emptyMessage = "Select the dishes from your recipe list",
        isEmpty = selections.isEmpty()
    ) {
        items(selections, key = { it.dish.id }) { selection ->
            DishSelectionRow(
                selection = selection,
                onQuantityChange = onQuantityChange,
                onToggle = onToggle,
                onRemove = onRemove
            )
        }
    }
}

@Composable
fun SupplySelectionTable(
    selections: List<SupplySelection>,
    onQuantityChange: (String, String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MonitoringSelectionTable(
        modifier = modifier,
        header = listOf("Name", "Quantity", ""),
        emptyMessage = "Select additional supplies from your inventory",
        isEmpty = selections.isEmpty()
    ) {
        items(selections, key = { it.supply.id }) { selection ->
            SupplySelectionRow(
                selection = selection,
                onQuantityChange = onQuantityChange,
                onToggle = onToggle,
                onRemove = onRemove
            )
        }
    }
}

@Composable
private fun MonitoringSelectionTable(
    modifier: Modifier = Modifier,
    header: List<String>,
    emptyMessage: String,
    isEmpty: Boolean,
    content: LazyColumn.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.background(Color.White)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MonitoringBackground)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                header.forEach { title ->
                    Text(
                        text = title.uppercase(),
                        style = MonitoringTableHeaderStyle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (isEmpty) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emptyMessage,
                        style = MonitoringBodyStyle,
                        color = MonitoringSecondaryText
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun DishSelectionRow(
    selection: DishSelection,
    onQuantityChange: (String, Int) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(
                text = selection.dish.name,
                style = MonitoringTableCellStyle
            )
            Text(
                text = String.format("S/ %.2f", selection.dish.unitPrice),
                style = MonitoringBodyStyle
            )
        }

        QuantitySelector(
            quantity = selection.quantity,
            onQuantityChange = { onQuantityChange(selection.dish.id, it) },
            modifier = Modifier.weight(1f)
        )

        Checkbox(
            checked = selection.isConfirmed,
            onCheckedChange = { onToggle(selection.dish.id) },
            colors = CheckboxDefaults.colors(
                checkedColor = MonitoringBlue,
                uncheckedColor = MonitoringBorder
            )
        )

        IconButton(onClick = { onRemove(selection.dish.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MonitoringRed
            )
        }
    }
}

@Composable
private fun SupplySelectionRow(
    selection: SupplySelection,
    onQuantityChange: (String, String) -> Unit,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(
                text = selection.supply.name,
                style = MonitoringTableCellStyle
            )
            selection.supply.measurementHint?.let {
                Text(
                    text = "Unidad: $it",
                    style = MonitoringBodyStyle,
                    color = MonitoringMutedText
                )
            }
        }

        TextInputQuantity(
            value = selection.quantity,
            onValueChange = { onQuantityChange(selection.supply.id, it) },
            modifier = Modifier.weight(1f)
        )

        Checkbox(
            checked = selection.isConfirmed,
            onCheckedChange = { onToggle(selection.supply.id) },
            colors = CheckboxDefaults.colors(
                checkedColor = MonitoringBlue,
                uncheckedColor = MonitoringBorder
            )
        )

        IconButton(onClick = { onRemove(selection.supply.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MonitoringRed
            )
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = null)
        }
        Text(
            text = quantity.toString(),
            style = MonitoringTableCellStyle,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
private fun TextInputQuantity(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = MonitoringTableCellStyle,
        singleLine = true
    )
}

