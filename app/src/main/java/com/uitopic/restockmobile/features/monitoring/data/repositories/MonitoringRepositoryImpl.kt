package com.uitopic.restockmobile.features.monitoring.data.repositories

import com.uitopic.restockmobile.features.monitoring.data.datasource.MonitoringLocalDataSource
import com.uitopic.restockmobile.features.monitoring.domain.models.SaleRecord
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesDish
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesSupply
import com.uitopic.restockmobile.features.monitoring.domain.repositories.MonitoringRepository
import javax.inject.Inject

class MonitoringRepositoryImpl @Inject constructor(
    private val localDataSource: MonitoringLocalDataSource
) : MonitoringRepository {

    override fun getAvailableDishes(): List<SalesDish> = localDataSource.getDishes()

    override fun getAvailableSupplies(): List<SalesSupply> = localDataSource.getSupplies()

    override fun registerSale(record: SaleRecord): SaleRecord = record
}

