package com.app.popular.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.core.utils.failures.Failure
import com.app.popular.domain.repository.PopularRepository
import com.app.popular.ui.states.PopularMovieState
import com.app.popular.ui.states.PopularState
import com.app.popular.ui.states.PopularTvShowState
import com.app.popular.ui.states.TopRatedMovieState
import com.app.popular.ui.states.TopRatedTvShowState
import com.app.popular.ui.states.TrendingDailyState
import com.app.popular.ui.states.TrendingWeeklyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PopularViewModel(
    private val popularRepository: PopularRepository
) : ViewModel() {

    private val _popularState = MutableStateFlow(PopularState())
    val popularState get() = _popularState.asStateFlow()

    init {
        fetchPopularMovies()
        fetchPopularTvShows()
        fetchTrendingDaily()
        fetchTrendingWeekly()
        fetchTopRatedMovies()
        fetchTopRatedTvShows()
    }

    fun fetchTrendingDaily() {
        _popularState.value = _popularState.value.copy(
            trendingDailyState = TrendingDailyState.Loading
        )

        viewModelScope.launch {
            try {
                val list = popularRepository.getTrendingDaily()
                Log.d("PopularViewModel", "fetchTrendingDaily: $list")
                _popularState.value = _popularState.value.copy(
                    trendingDailyState = if (list.isNotEmpty()) {
                        TrendingDailyState.Success(list)
                    } else {
                        TrendingDailyState.Empty
                    }
                )
            } catch (e: Throwable) {
                _popularState.value = _popularState.value.copy(
                    trendingDailyState = when (e) {
                        is Failure -> TrendingDailyState.Error(e.message ?: "Unknown error")
                        else -> TrendingDailyState.Error("An unexpected error occurred")
                    }
                )
            }
        }
    }

    fun fetchTrendingWeekly() {
        _popularState.value = _popularState.value.copy(
            trendingWeeklyState = TrendingWeeklyState.Loading
        )

        viewModelScope.launch {
            try {
                val list = popularRepository.getTrendingWeekly()
                _popularState.value = _popularState.value.copy(
                    trendingWeeklyState = if (list.isNotEmpty()) {
                        TrendingWeeklyState.Success(list)
                    } else {
                        TrendingWeeklyState.Empty
                    }
                )
            } catch (e: Throwable) {
                _popularState.value = _popularState.value.copy(
                    trendingWeeklyState = when (e) {
                        is Failure -> TrendingWeeklyState.Error(e.message ?: "Unknown error")
                        else -> TrendingWeeklyState.Error("An unexpected error occurred")
                    }
                )
            }
        }
    }

    fun fetchPopularMovies() {
        _popularState.value = _popularState.value.copy(
            popularMovieState = PopularMovieState.Loading
        )

        viewModelScope.launch {
            try {
                val list = popularRepository.getPopularMovies()
                _popularState.value = _popularState.value.copy(
                    popularMovieState = if (list.isNotEmpty()) {
                        PopularMovieState.Success(list)
                    } else {
                        PopularMovieState.Empty
                    }
                )
            } catch (e: Throwable) {
                _popularState.value = _popularState.value.copy(
                    popularMovieState = when (e) {
                        is Failure -> PopularMovieState.Error(e.message ?: "Unknown error")
                        else -> PopularMovieState.Error("An unexpected error occurred")
                    }
                )
            }
        }
    }

    fun fetchPopularTvShows() {
        _popularState.value = _popularState.value.copy(
            popularTvShowState = PopularTvShowState.Loading
        )

        viewModelScope.launch {
            try {
                val list = popularRepository.getPopularTvShows()
                _popularState.value = _popularState.value.copy(
                    popularTvShowState = if (list.isNotEmpty()) {
                        PopularTvShowState.Success(list)
                    } else {
                        PopularTvShowState.Empty
                    }
                )
            } catch (e: Throwable) {
                _popularState.value = _popularState.value.copy(
                    popularTvShowState = when (e) {
                        is Failure -> PopularTvShowState.Error(e.message ?: "Unknown error")
                        else -> PopularTvShowState.Error("An unexpected error occurred")
                    }
                )
            }
        }
    }

    fun fetchTopRatedMovies() {
        _popularState.value = _popularState.value.copy(
            topRatedMovieState = TopRatedMovieState.Loading
        )

        viewModelScope.launch {
            try {
                val list = popularRepository.getTopRatedMovies()
                _popularState.value = _popularState.value.copy(
                    topRatedMovieState = if (list.isNotEmpty()) {
                        TopRatedMovieState.Success(list)
                    } else {
                        TopRatedMovieState.Empty
                    }
                )
            } catch (e: Throwable) {
                _popularState.value = _popularState.value.copy(
                    topRatedMovieState = when (e) {
                        is Failure -> TopRatedMovieState.Error(e.message ?: "Unknown error")
                        else -> TopRatedMovieState.Error("An unexpected error occurred")
                    }
                )
            }
        }
    }

    fun fetchTopRatedTvShows() {
        _popularState.value = _popularState.value.copy(
            topRatedTvShowState = TopRatedTvShowState.Loading
        )

        viewModelScope.launch {
            try {
                val list = popularRepository.getTopRatedTvShows()
                _popularState.value = _popularState.value.copy(
                    topRatedTvShowState = if (list.isNotEmpty()) {
                        TopRatedTvShowState.Success(list)
                    } else {
                        TopRatedTvShowState.Empty
                    }
                )
            } catch (e: Throwable) {
                _popularState.value = _popularState.value.copy(
                    topRatedTvShowState = when (e) {
                        is Failure -> TopRatedTvShowState.Error(e.message ?: "Unknown error")
                        else -> TopRatedTvShowState.Error("An unexpected error occurred")
                    }
                )
            }

        }
    }
}