package com.app.popular.ui.states

import com.app.core.domain.entities.Media

data class PopularState(
    val popularMovieState: PopularMovieState = PopularMovieState.Empty,
    val popularTvShowState: PopularTvShowState = PopularTvShowState.Empty,
    val trendingDailyState: TrendingDailyState = TrendingDailyState.Empty,
    val trendingWeeklyState: TrendingWeeklyState = TrendingWeeklyState.Empty,
    val topRatedMovieState: TopRatedMovieState = TopRatedMovieState.Empty,
    val topRatedTvShowState: TopRatedTvShowState = TopRatedTvShowState.Empty
)

sealed class PopularMovieState {
    data class Success(val mediaList: List<Media>) : PopularMovieState()
    data class Error(val message: String) : PopularMovieState()
    object Loading : PopularMovieState()
    object Empty : PopularMovieState()
}

sealed class PopularTvShowState {
    data class Success(val mediaList: List<Media>) : PopularTvShowState()
    data class Error(val message: String) : PopularTvShowState()
    object Loading : PopularTvShowState()
    object Empty : PopularTvShowState()
}

sealed class TrendingDailyState {
    data class Success(val mediaList: List<Media>) : TrendingDailyState()
    data class Error(val message: String) : TrendingDailyState()
    object Loading : TrendingDailyState()
    object Empty : TrendingDailyState()
}

sealed class TrendingWeeklyState {
    data class Success(val mediaList: List<Media>) : TrendingWeeklyState()
    data class Error(val message: String) : TrendingWeeklyState()
    object Loading : TrendingWeeklyState()
    object Empty : TrendingWeeklyState()
}

sealed class TopRatedMovieState {
    data class Success(val mediaList: List<Media>) : TopRatedMovieState()
    data class Error(val message: String) : TopRatedMovieState()
    object Loading : TopRatedMovieState()
    object Empty : TopRatedMovieState()
}

sealed class TopRatedTvShowState {
    data class Success(val mediaList: List<Media>) : TopRatedTvShowState()
    data class Error(val message: String) : TopRatedTvShowState()
    object Loading : TopRatedTvShowState()
    object Empty : TopRatedTvShowState()
}