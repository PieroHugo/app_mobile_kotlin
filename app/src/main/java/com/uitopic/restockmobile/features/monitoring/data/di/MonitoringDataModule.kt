package com.uitopic.restockmobile.features.monitoring.data.di

import com.uitopic.restockmobile.features.monitoring.data.datasource.MonitoringLocalDataSource
import com.uitopic.restockmobile.features.monitoring.data.repositories.MonitoringRepositoryImpl
import com.uitopic.restockmobile.features.monitoring.domain.repositories.MonitoringRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MonitoringDataModule {

    @Binds
    @Singleton
    abstract fun bindMonitoringRepository(
        impl: MonitoringRepositoryImpl
    ): MonitoringRepository

    companion object {
        @Provides
        @Singleton
        fun provideMonitoringLocalDataSource(): MonitoringLocalDataSource = MonitoringLocalDataSource()
    }
}

