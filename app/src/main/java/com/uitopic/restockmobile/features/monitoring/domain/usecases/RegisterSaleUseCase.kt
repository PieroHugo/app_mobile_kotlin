package com.uitopic.restockmobile.features.monitoring.domain.usecases

import com.uitopic.restockmobile.features.monitoring.domain.models.SaleRecord
import com.uitopic.restockmobile.features.monitoring.domain.repositories.MonitoringRepository
import javax.inject.Inject

class RegisterSaleUseCase @Inject constructor(
    private val repository: MonitoringRepository
) {
    operator fun invoke(record: SaleRecord): SaleRecord = repository.registerSale(record)
}

