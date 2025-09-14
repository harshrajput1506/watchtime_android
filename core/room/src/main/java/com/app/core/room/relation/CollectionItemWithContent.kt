package com.app.core.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.app.core.room.entities.CollectionItemEntity
import com.app.core.room.entities.ContentMetadataEntity

data class CollectionItemWithContent(
    @Embedded val item: CollectionItemEntity,
    @Relation(
        parentColumn = "content_id",
        entityColumn = "id"
    )
    val content: ContentMetadataEntity
)
