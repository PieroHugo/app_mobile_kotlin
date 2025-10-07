package com.uitopic.restockmobile.features.monitoring.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberUpdatedState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
data class MonitoringDropdownOption(
    val id: String,
    val label: String
)

fun MonitoringDropdown(
    label: String,
    placeholder: String,
    options: List<MonitoringDropdownOption>,
    selectedOptionId: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val currentSelection = rememberUpdatedState(
        options.find { it.id == selectedOptionId }?.label
    )

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MonitoringLabelStyle,
            fontFamily = MonitoringFontFamily,
            fontWeight = FontWeight.Medium
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = setExpanded,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = currentSelection.value?.takeIf { it.isNotBlank() } ?: "",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                textStyle = MonitoringBodyStyle,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MonitoringBodyStyle
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label,
                                style = MonitoringBodyStyle,
                                color = MonitoringPrimaryText
                            )
                        },
                        onClick = {
                            onOptionSelected(option.id)
                            setExpanded(false)
                        }
                    )
                }
            }
        }
    }
}

