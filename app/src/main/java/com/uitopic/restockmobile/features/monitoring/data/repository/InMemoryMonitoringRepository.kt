package com.uitopic.restockmobile.features.monitoring.data.repository

import com.uitopic.restockmobile.features.monitoring.domain.model.AdditionalSupply
import com.uitopic.restockmobile.features.monitoring.domain.model.Dish
import com.uitopic.restockmobile.features.monitoring.domain.repository.MonitoringRepository
import javax.inject.Inject

class InMemoryMonitoringRepository @Inject constructor() : MonitoringRepository {

    private val dishes = listOf(
        Dish(id = "dish-1", name = "Lomo Saltado", unitPrice = 20.50),
        Dish(id = "dish-2", name = "Arroz con Pollo", unitPrice = 21.50),
        Dish(id = "dish-3", name = "Sopa dieta", unitPrice = 15.50)
    )

    private val supplies = listOf(
        AdditionalSupply(id = "supply-1", name = "Huevo", defaultQuantityLabel = "1"),
        AdditionalSupply(id = "supply-2", name = "Arroz", defaultQuantityLabel = "250 g"),
        AdditionalSupply(id = "supply-3", name = "Inka cola personal", defaultQuantityLabel = "3")
    )

    override fun getDishes(): List<Dish> = dishes

    override fun getAdditionalSupplies(): List<AdditionalSupply> = supplies
}
