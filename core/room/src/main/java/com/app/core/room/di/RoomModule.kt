package com.app.core.room.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.core.room.dao.CollectionDao
import com.app.core.room.database.AppDatabase
import org.koin.dsl.module

val roomModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            "watchtime_database"
        ).build()
    }

    single<CollectionDao> {
        get<AppDatabase>().collectionDao()
    }
}