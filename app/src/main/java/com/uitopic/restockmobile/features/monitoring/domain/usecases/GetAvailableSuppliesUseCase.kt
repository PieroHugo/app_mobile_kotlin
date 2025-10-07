package com.uitopic.restockmobile.features.monitoring.domain.usecases

import com.uitopic.restockmobile.features.monitoring.domain.models.SalesSupply
import com.uitopic.restockmobile.features.monitoring.domain.repositories.MonitoringRepository
import javax.inject.Inject

class GetAvailableSuppliesUseCase @Inject constructor(
    private val repository: MonitoringRepository
) {
    operator fun invoke(): List<SalesSupply> = repository.getAvailableSupplies()
}

