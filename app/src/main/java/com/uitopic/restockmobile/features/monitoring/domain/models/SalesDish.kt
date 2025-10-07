package com.uitopic.restockmobile.features.monitoring.domain.models

import java.util.UUID

data class SalesDish(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val unitPrice: Double
)

data class DishSelection(
    val dish: SalesDish,
    val quantity: Int,
    val isConfirmed: Boolean
)

