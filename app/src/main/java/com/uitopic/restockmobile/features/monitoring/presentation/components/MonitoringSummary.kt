package com.uitopic.restockmobile.features.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uitopic.restockmobile.features.monitoring.domain.models.DishSelection
import com.uitopic.restockmobile.features.monitoring.domain.models.SaleRecord
import com.uitopic.restockmobile.features.monitoring.domain.models.SupplySelection

@Composable
fun MonitoringSuccessSummary(
    record: SaleRecord,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MonitoringGreen,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sale successfully registered",
            color = MonitoringPrimaryText,
            fontFamily = MonitoringFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        SummaryTable(
            title = "Dishes",
            headers = listOf("Name", "Unit price", "Quantity"),
            dishes = record.dishes,
            supplies = emptyList()
        )

        Spacer(modifier = Modifier.height(16.dp))

        SummaryTable(
            title = "Additional supplies",
            headers = listOf("Name", "Quantity"),
            dishes = emptyList(),
            supplies = record.supplies
        )
    }
}

@Composable
private fun SummaryTable(
    title: String,
    headers: List<String>,
    dishes: List<DishSelection>,
    supplies: List<SupplySelection>
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MonitoringSubtitleStyle,
                fontWeight = FontWeight.SemiBold,
                color = MonitoringPrimaryText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MonitoringBackground)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                headers.forEach { header ->
                    Text(
                        text = header.uppercase(),
                        style = MonitoringTableHeaderStyle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (dishes.isEmpty() && supplies.isEmpty()) {
                Text(
                    text = "No records",
                    style = MonitoringBodyStyle,
                    modifier = Modifier.padding(16.dp)
                )
            }

            dishes.forEach { selection ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selection.dish.name,
                        style = MonitoringTableCellStyle,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = String.format("S/ %.2f", selection.dish.unitPrice),
                        style = MonitoringTableCellStyle,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = selection.quantity.toString(),
                        style = MonitoringTableCellStyle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            supplies.forEach { selection ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selection.supply.name,
                        style = MonitoringTableCellStyle,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = selection.quantity,
                        style = MonitoringTableCellStyle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

