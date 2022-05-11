package com.bignerdranch.android.bestmovies.presenters

import android.util.Log
import androidx.lifecycle.Observer
import com.bignerdranch.android.bestmovies.models.Film
import com.bignerdranch.android.bestmovies.models.Repository
import com.bignerdranch.android.bestmovies.views.IMovieListFragmentView
import moxy.MvpPresenter

class MovieListFragmentPresenter : MvpPresenter<IMovieListFragmentView>() {
//В ПРЕЗЕНТЕРЕ НЕ ДОЛЖНО БЫТЬ ССЫЛОК НА  Context/Activity/Fragment!!!

    //с учётом того, что Presenter полностью отвязан от жизненного цикла Activity, вы можете спокойно создавать экземпляр конкретной Model внутри Presenter и работать с ним.Используя DI вы можете подключать нужную Model в Presenter
    val repository = Repository.get() //Если есть даггер то @Inject lateinit var repository: Repository

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.startDataFetching()

        val filmsDataObserver = Observer<List<Film>>{ filmList->// observer за данными из DataFetcher
            Log.d("MovieListFragmentPresen", "Data recived")
            viewState.onSuccess(filmList)
        }
        repository.fetchData().observeForever(filmsDataObserver)
    }

}