package com.app.discover.domain.entities

data class DiscoverFilters(
    val sortBy: String = "popularity.desc",
    val genres: List<String> = emptyList(),
    val year: Int? = null,
    val voteAverageGte: Float? = null,
    val voteAverageLte: Float? = null,
    val includeAdult: Boolean = false
)

enum class SortOption(val value: String, val displayName: String) {
    POPULARITY_DESC("popularity.desc", "Popularity (High to Low)"),
    POPULARITY_ASC("popularity.asc", "Popularity (Low to High)"),
    RELEASE_DATE_DESC("release_date.desc", "Release Date (Newest)"),
    RELEASE_DATE_ASC("release_date.asc", "Release Date (Oldest)"),
    VOTE_AVERAGE_DESC("vote_average.desc", "Rating (High to Low)"),
    VOTE_AVERAGE_ASC("vote_average.asc", "Rating (Low to High)"),
    TITLE_ASC("title.asc", "Title (A-Z)"),
    TITLE_DESC("title.desc", "Title (Z-A)")
}
