package com.bignerdranch.android.bestmovies.presenters

import androidx.lifecycle.Observer
import com.bignerdranch.android.bestmovies.models.Film
import com.bignerdranch.android.bestmovies.models.Repository
import com.bignerdranch.android.bestmovies.views.IMovieListFragmentView
import moxy.MvpPresenter

// MovieListFragmentPresenter будет жить пока есть View, в которой он содержится (а также он переживет смену конфигурации)
class MovieListFragmentPresenter : MvpPresenter<IMovieListFragmentView>() {

    //Presenter полностью отвязан от жизненного цикла Фрагмента, и мы можем спокойно создавать экземпляр конкретной Model внутри Presenter и работать с ним.
    val repository = Repository.get() //Используя DI мы можем подключать нужную Model в Presenter.

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.startDataFetching()// отображаем ProgressBar на время получения данных

        val filmsDataObserver = Observer<List<Film>>{ filmList->// observer за LiveData из DataFetcher
            if(filmList.isEmpty()){// если данных нет, отображаем AlertDialog
                viewState.onFailure("Сетевая ошибка, неудалось получить данные.\n" +
                        "Проверте интернет-соединение или попробуйте позже.")
            }else {
                viewState.onSuccess(filmList.sortedBy { it.localized_name })// передаем во view список всех фильмов отсортированнх по названию
            }
        }
        repository.fetchData().observeForever(filmsDataObserver)// подключаем observer к LiveData
    }

    var checkedGenre: String = ""// в это поле сохраняем выбранный жанр (в presenter оно переживет пересоздание view)

    fun getFilmsByGenre(films: List<Film>, genre: String): List<Film>{// получаем список фильмов соответсвующих указанному жанру
        val filmListByGenre = mutableListOf<Film>()
        for (film in films){
            if (film.genres.contains(genre))
                filmListByGenre.add(film)
        }
        return filmListByGenre
    }

    fun getAllGenres(films: List<Film>): Set<String>{ // извлекаем множество жанров из общего списка фильмов
        val genresSet = mutableSetOf<String>()
        for (film in films)
            genresSet.addAll(film.genres) // множество содержит только уникальные значения, поэтому все дубликаты будут игнорироваться
        return genresSet
    }

}