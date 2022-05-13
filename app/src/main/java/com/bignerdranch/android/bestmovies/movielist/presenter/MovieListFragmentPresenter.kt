package com.bignerdranch.android.bestmovies.movielist.presenter

import androidx.lifecycle.Observer
import com.bignerdranch.android.bestmovies.movielist.model.Genre
import com.bignerdranch.android.bestmovies.movielist.model.Movie
import com.bignerdranch.android.bestmovies.movielist.model.RecyclerDataModel
import com.bignerdranch.android.bestmovies.movielist.model.Title
import com.bignerdranch.android.bestmovies.utils.Film
import com.bignerdranch.android.bestmovies.utils.Repository
import com.bignerdranch.android.bestmovies.movielist.view.IMovieListFragmentView
import moxy.MvpPresenter

// MovieListFragmentPresenter будет жить пока есть View, в которой он содержится (а также он переживет смену конфигурации)
class MovieListFragmentPresenter : MvpPresenter<IMovieListFragmentView>() {

    //Presenter полностью отвязан от жизненного цикла Фрагмента, и мы можем спокойно создавать экземпляр конкретной Model внутри Presenter и работать с ним.
    private val repository = Repository.get() //Используя DI мы можем подключать нужную Model в Presenter.
    private lateinit var allFilms: List<Film> //здесь хранятся полученные данные

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.startDataFetching()// отображаем ProgressBar на время получения данных

        val filmsDataObserver = Observer<List<Film>>{ filmList->// observer за LiveData из DataFetcher
            if(filmList.isEmpty()){// если данных нет, отображаем AlertDialog
                viewState.onFailure("Сетевая ошибка, неудалось получить данные.\n" +
                        "Проверте интернет-соединение или попробуйте позже.")
            }else {
                allFilms = filmList.sortedBy { it.localized_name } // сохраняем полученные фильмы
                prepareRecyclerData() // собираем данные для RecylerView
                viewState.onSuccess(adapterDataFilterPart, adapterDataMoviesPart)// передаем во view список фильтров с заголовками и список фильмов
            }
        }
        repository.fetchData().observeForever(filmsDataObserver)// подключаем observer к LiveData
    }

    private val adapterDataFilterPart = mutableListOf<RecyclerDataModel>()
    private val adapterDataMoviesPart = mutableListOf<RecyclerDataModel>()

    fun prepareRecyclerData(){// собираем данные для RecyclerView
        adapterDataFilterPart.addAll(getFilterDataPart(""))
        adapterDataMoviesPart.addAll(getMoviesByGenreDataPart(""))
    }

    fun getFilterDataPart(checkedItem: String): List<RecyclerDataModel>{// получаем список жанров для RecyclerVeiw (над жанрами имеется заголовок "Жанры", а снизу "Фильмы")
        if(adapterDataFilterPart.isEmpty()){// если список ранее не собирался
            val genresWithTitles = mutableListOf<RecyclerDataModel>()
            genresWithTitles.add(Title("Жанры"))// Добавляем заголвок "Жанры"
            for (genre in getAllGenres()){
                genresWithTitles.add(Genre(genre, false))// Добавляем все жанры
            }
            genresWithTitles.add(Title("Фильмы"))// Добавляем заголвок "Фильмы"
            return genresWithTitles
        }else{// в случае если был выбран жанр из существующего списка (или в случае пересоздания)
            for (i in (1..(adapterDataFilterPart.size-2))){//проходим по всем жанрам расположенным между заголовками (-2 т.к. считаем от 0 и еще последний элемент это заголовок "Фильмы")
                val genreItem = adapterDataFilterPart[i] as Genre
                if(genreItem.title == checkedItem){
                    adapterDataFilterPart[i] = Genre(genreItem.title, true)// запоминаем выбранный жанр
                }
                else{
                    adapterDataFilterPart[i] = Genre(genreItem.title, false)
                }
            }
            return adapterDataFilterPart
        }
    }

    fun getMoviesByGenreDataPart(genre: String): List<RecyclerDataModel>{// получаем список фильмов для RecyclerVeiw соответсвующего жанра
        if (adapterDataMoviesPart.isEmpty()){//если список ранее не собирался то добавляем все фильмы подряд
            val moviesByGenreData: MutableList<RecyclerDataModel> = mutableListOf()
                for (film in allFilms)
                    moviesByGenreData.add(Movie(film))
            return moviesByGenreData
        }else{// в случае если был выбран жанр
            adapterDataMoviesPart.clear()// очищаем старый список
            for (film in getMoviesByGenre(genre)){ // формируем новый
                adapterDataMoviesPart.add(Movie(film))
            }
            return adapterDataMoviesPart
        }
    }

    fun getMoviesByGenre(genre: String): List<Film>{// получаем список фильмов соответсвующих указанному жанру
        val filmListByGenre = mutableListOf<Film>()
        for (film in allFilms){
            if (film.genres.contains(genre))
                filmListByGenre.add(film)
        }
        return filmListByGenre
    }

    fun getAllGenres(): Set<String>{ // извлекаем множество жанров из общего списка фильмов
        val genresSet = mutableSetOf<String>()
        for (film in allFilms)
            genresSet.addAll(film.genres) // множество содержит только уникальные значения, поэтому все дубликаты будут игнорироваться
        return genresSet
    }

}