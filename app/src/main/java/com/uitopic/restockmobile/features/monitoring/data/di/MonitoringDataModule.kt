package com.uitopic.restockmobile.features.monitoring.data.di

import com.uitopic.restockmobile.features.monitoring.data.repository.InMemoryMonitoringRepository
import com.uitopic.restockmobile.features.monitoring.domain.repository.MonitoringRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MonitoringDataModule {

    @Binds
    @Singleton
    abstract fun bindMonitoringRepository(
        repository: InMemoryMonitoringRepository
    ): MonitoringRepository
}
