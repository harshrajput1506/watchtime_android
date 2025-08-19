package com.app.collections.domain.models

open data class Collection(
    val id: String,
    val name: String,
    val titles: List<Title>,
)

data class Title(
    val id: Int,
    val name: String,
    val posterPath: String? = null,
)
