package com.bignerdranch.android.bestmovies.movielist.view

import com.bignerdranch.android.bestmovies.movielist.model.RecyclerDataModel
import moxy.MvpView
import moxy.viewstate.strategy.*

@StateStrategyType(value = AddToEndSingleStrategy::class)// команды выполняются повторно для пересозданной view, в очереди только один экземпляр одной команды
interface IMovieListFragmentView: MvpView {
    @StateStrategyType(value = OneExecutionStateStrategy::class)//выполняется ровно один раз, очередь команд остается неизменной
    fun startDataFetching() // запрос данных делаем только один раз в событии onFirstViewAttach в Presenter
    fun onFailure(message: String)//  при пересоздании view ошибка появится снова
    fun onSuccess(firsDataPart: List<RecyclerDataModel>, secondDataPart: List<RecyclerDataModel>) // при пересоздании view занаво сформируется RecyclerView с учетом изменившейся конфигурации
}