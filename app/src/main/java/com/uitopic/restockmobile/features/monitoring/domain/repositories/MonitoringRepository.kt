package com.uitopic.restockmobile.features.monitoring.domain.repositories

import com.uitopic.restockmobile.features.monitoring.domain.models.SaleRecord
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesDish
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesSupply

interface MonitoringRepository {
    fun getAvailableDishes(): List<SalesDish>
    fun getAvailableSupplies(): List<SalesSupply>
    fun registerSale(record: SaleRecord): SaleRecord
}

