package com.uitopic.restockmobile.features.monitoring.domain.models

data class SaleRecord(
    val dishes: List<DishSelection>,
    val supplies: List<SupplySelection>
) {
    val totalAmount: Double = dishes.sumOf { it.dish.unitPrice * it.quantity }
}

