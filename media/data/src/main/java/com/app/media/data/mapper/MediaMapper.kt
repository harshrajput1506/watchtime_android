package com.app.media.data.mapper

import com.app.core.network.model.CastMemberResponse
import com.app.core.network.model.CountryWatchProvidersResponse
import com.app.core.network.model.CreatedBy
import com.app.core.network.model.CreditsResponse
import com.app.core.network.model.CrewMemberResponse
import com.app.core.network.model.EpisodeResponse
import com.app.core.network.model.GenreResponse
import com.app.core.network.model.MovieDetailsResponse
import com.app.core.network.model.NetworkResponse
import com.app.core.network.model.SeasonDetailsResponse
import com.app.core.network.model.SeasonResponse
import com.app.core.network.model.TvShowDetailsResponse
import com.app.core.network.model.WatchProviderResponse
import com.app.core.network.model.WatchProvidersResponse
import com.app.media.domain.model.Cast
import com.app.media.domain.model.CastMember
import com.app.media.domain.model.CountryProviders
import com.app.media.domain.model.Creator
import com.app.media.domain.model.CrewMember
import com.app.media.domain.model.Episode
import com.app.media.domain.model.Genre
import com.app.media.domain.model.MediaDetails
import com.app.media.domain.model.Network
import com.app.media.domain.model.Provider
import com.app.media.domain.model.Season
import com.app.media.domain.model.SeasonDetails
import com.app.media.domain.model.WatchProviders

fun MovieDetailsResponse.toMediaDetails(): MediaDetails {
    return MediaDetails(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genres = genres.map { it.toDomainGenre() },
        runtime = runtime,
        status = status,
        tagline = tagline,
        homepage = homepage
    )
}

fun TvShowDetailsResponse.toMediaDetails(): MediaDetails {
    return MediaDetails(
        id = id,
        title = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = firstAirDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genres = genres.map { it.toDomainGenre() },
        numberOfSeasons = numberOfSeasons,
        numberOfEpisodes = numberOfEpisodes,
        status = status,
        tagline = tagline,
        homepage = homepage,
        seasons = seasons.map { it.toDomainSeason() },
        networks = networks.map { it.toDomainNetwork() },
        createdBy = createdBy.map { it.toDomainCreator() }
    )
}

fun GenreResponse.toDomainGenre(): Genre {
    return com.app.media.domain.model.Genre(
        id = id,
        name = name
    )
}

fun SeasonResponse.toDomainSeason(): Season {
    return Season(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        seasonNumber = seasonNumber,
        episodeCount = episodeCount,
        airDate = airDate,
        voteAverage = voteAverage
    )
}

fun NetworkResponse.toDomainNetwork(): Network {
    return Network(
        id = id,
        name = name,
        logoPath = logoPath
    )
}

fun CreatedBy.toDomainCreator(): Creator {
    return Creator(
        id = id,
        name = name,
        profilePath = profilePath
    )
}

fun CreditsResponse.toCast(): Cast {
    return Cast(
        id = id,
        cast = cast.map { it.toDomainCastMember() },
        crew = crew.map { it.toDomainCrewMember() }
    )
}

fun CastMemberResponse.toDomainCastMember(): CastMember {
    return com.app.media.domain.model.CastMember(
        id = id,
        name = name,
        profilePath = profilePath,
        character = character,
        order = order
    )
}

fun CrewMemberResponse.toDomainCrewMember(): CrewMember {
    return CrewMember(
        id = id,
        name = name,
        profilePath = profilePath,
        job = job,
        department = department
    )
}

fun WatchProvidersResponse.toWatchProviders(): WatchProviders {
    return WatchProviders(
        id = id,
        countryProviders = results.mapValues { (_, providers) ->
            providers.toCountryProviders()
        }
    )
}

fun CountryWatchProvidersResponse.toCountryProviders(): CountryProviders {
    return CountryProviders(
        link = link,
        streaming = flatRate?.map { it.toProvider() } ?: emptyList(),
        buy = buy?.map { it.toProvider() } ?: emptyList(),
        rent = rent?.map { it.toProvider() } ?: emptyList()
    )
}

fun WatchProviderResponse.toProvider(): Provider {
    return Provider(
        logoPath = logoPath,
        providerId = providerId,
        providerName = providerName
    )
}

fun SeasonDetailsResponse.toSeasonDetails(): SeasonDetails {
    return SeasonDetails(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        seasonNumber = seasonNumber,
        airDate = airDate,
        episodes = episodes.map { it.toDomainEpisode() }
    )
}

fun EpisodeResponse.toDomainEpisode(): Episode {
    return Episode(
        id = id,
        name = name,
        overview = overview,
        episodeNumber = episodeNumber,
        airDate = airDate,
        runtime = runtime,
        stillPath = stillPath,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}
