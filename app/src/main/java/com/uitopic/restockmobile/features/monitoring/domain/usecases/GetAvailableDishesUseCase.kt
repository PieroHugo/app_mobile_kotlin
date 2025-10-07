package com.uitopic.restockmobile.features.monitoring.domain.usecases

import com.uitopic.restockmobile.features.monitoring.domain.models.SalesDish
import com.uitopic.restockmobile.features.monitoring.domain.repositories.MonitoringRepository
import javax.inject.Inject

class GetAvailableDishesUseCase @Inject constructor(
    private val repository: MonitoringRepository
) {
    operator fun invoke(): List<SalesDish> = repository.getAvailableDishes()
}

