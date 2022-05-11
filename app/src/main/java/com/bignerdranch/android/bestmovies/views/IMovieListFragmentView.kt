package com.bignerdranch.android.bestmovies.views

import com.bignerdranch.android.bestmovies.models.Film
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface IMovieListFragmentView: MvpView {
    fun startDataFetching()
    fun onError(message: String)
    fun onSuccess(films: List<Film>)
}