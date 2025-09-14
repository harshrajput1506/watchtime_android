package com.app.collections.data.module

import com.app.collections.data.repository.CollectionRepositoryImpl
import com.app.collections.domain.repository.CollectionRepository
import com.app.core.room.database.AppDatabase
import org.koin.dsl.module

val collectionDataModule = module {
    single<CollectionRepository> {
        CollectionRepositoryImpl(get<AppDatabase>().collectionDao())
    }
}