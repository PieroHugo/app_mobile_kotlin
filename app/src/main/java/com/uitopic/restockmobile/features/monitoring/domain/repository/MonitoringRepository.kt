package com.uitopic.restockmobile.features.monitoring.domain.repository

import com.uitopic.restockmobile.features.monitoring.domain.model.AdditionalSupply
import com.uitopic.restockmobile.features.monitoring.domain.model.Dish

interface MonitoringRepository {
    fun getDishes(): List<Dish>
    fun getAdditionalSupplies(): List<AdditionalSupply>
}
