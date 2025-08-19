package com.app.collections.data.models

data class CollectionDTO(
    val id: String,
    val uid: String = "",
    val name: String,
    val titles: List<TitleDTO>
)

data class TitleDTO(
    val id: Int,
    val name: String,
    val posterPath: String? = null
)

// Mapper
fun CollectionDTO.toDomainModel(): com.app.collections.domain.models.Collection {
    return com.app.collections.domain.models.Collection(
        id = id,
        name = name,
        titles = titles.map { it.toDomainModel() }
    )
}

fun TitleDTO.toDomainModel(): com.app.collections.domain.models.Title {
    return com.app.collections.domain.models.Title(
        id = id,
        name = name,
        posterPath = posterPath
    )
}