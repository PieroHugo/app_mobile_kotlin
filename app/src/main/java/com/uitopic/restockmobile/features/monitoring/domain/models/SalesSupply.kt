package com.uitopic.restockmobile.features.monitoring.domain.models

data class SalesSupply(
    val id: String,
    val name: String,
    val measurementHint: String? = null
)

data class SupplySelection(
    val supply: SalesSupply,
    val quantity: String,
    val isConfirmed: Boolean
)

