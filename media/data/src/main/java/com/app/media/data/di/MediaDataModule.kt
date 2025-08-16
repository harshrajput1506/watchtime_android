package com.app.media.data.di

import com.app.core.network.api.TMDBApiService
import com.app.media.data.data_source.MediaRemoteDataSource
import com.app.media.data.repository.MediaRepositoryImpl
import com.app.media.domain.repository.MediaRepository
import org.koin.dsl.module

val mediaDataModule = module {
    single<MediaRemoteDataSource> { MediaRemoteDataSource(get<TMDBApiService>()) }
    single<MediaRepository> { MediaRepositoryImpl(get<MediaRemoteDataSource>()) }
}
