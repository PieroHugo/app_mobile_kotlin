package com.uitopic.restockmobile.features.monitoring.domain.model

data class SaleSummary(
    val dishes: List<SaleDishSummary>,
    val supplies: List<SaleSupplySummary>
)

data class SaleDishSummary(
    val name: String,
    val unitPrice: Double,
    val quantity: Int
)

data class SaleSupplySummary(
    val name: String,
    val quantity: String
)
