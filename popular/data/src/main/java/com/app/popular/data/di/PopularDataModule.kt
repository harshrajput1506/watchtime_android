package com.app.popular.data.di

import com.app.core.network.api.TMDBApiService
import com.app.popular.data.data_source.RemoteDataSource
import com.app.popular.data.repository.PopularRepositoryImpl
import com.app.popular.domain.repository.PopularRepository
import org.koin.dsl.module

val popularDataModule = module {

    // Remote Data Source
    single<RemoteDataSource> {
        RemoteDataSource(tmdbApiService = get<TMDBApiService>())
    }

    // Repository
    single<PopularRepository> {
        PopularRepositoryImpl(remoteDataSource = get<RemoteDataSource>())
    }
}
