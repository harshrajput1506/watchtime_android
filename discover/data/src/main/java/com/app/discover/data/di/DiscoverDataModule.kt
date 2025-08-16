package com.app.discover.data.di

import com.app.discover.data.data_source.DiscoverRemoteDataSource
import com.app.discover.data.repository.DiscoverRepositoryImpl
import com.app.discover.domain.repository.DiscoverRepository
import org.koin.dsl.module

val discoverDataModule = module {
    single<DiscoverRemoteDataSource> {
        DiscoverRemoteDataSource(get())
    }

    single<DiscoverRepository> {
        DiscoverRepositoryImpl(get())
    }
}
