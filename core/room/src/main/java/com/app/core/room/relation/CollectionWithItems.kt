package com.app.core.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.app.core.room.entities.CollectionEntity
import com.app.core.room.entities.CollectionItemEntity

data class CollectionWithItems(
    @Embedded val collection: CollectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "collection_id",
        entity = CollectionItemEntity::class
    )
    val items: List<CollectionItemWithContent>
)
