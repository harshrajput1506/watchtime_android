package com.app.core.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.core.room.dao.CollectionDao
import com.app.core.room.entities.CollectionEntity
import com.app.core.room.entities.CollectionItemEntity
import com.app.core.room.entities.ContentMetadataEntity

@Database(
    entities = [CollectionEntity::class, CollectionItemEntity::class, ContentMetadataEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao() : CollectionDao
}