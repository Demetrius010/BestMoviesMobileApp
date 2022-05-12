package com.bignerdranch.android.bestmovies.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.databinding.FragmentMovieListBinding
import com.bignerdranch.android.bestmovies.models.Film
import com.bignerdranch.android.bestmovies.presenters.MovieListFragmentPresenter
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class MovieListFragment : MvpAppCompatFragment(), IMovieListFragmentView{
    private var _binding: FragmentMovieListBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    @InjectPresenter//Инжектим Presenter через Moxy
    lateinit var presenter: MovieListFragmentPresenter

    lateinit var radioGroup: RadioGroup // фильтр из жанров выполнен на основе RadioButton которые помещаются в этот RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        radioGroup = RadioGroup(context)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.movie_list_frigment_title) // меняем title т.к. в MovieDetailsFragment он менялся на название фильма
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun startDataFetching() { // показываем ProgressBar пока получаем данных
        showProgressBar(true)
    }

    override fun onFailure(message: String) { // в случае неудачи (например: нет интернет соединения) показываем AlertDialog
        showProgressBar(false)
        AlertDialog.Builder(requireContext())
            .setTitle("Ошибка!")
            .setMessage(message)
            .setNegativeButton("Ok", null)
            .show()
    }

    override fun onSuccess(films: List<Film>) { // как только получили данные скрываем ProgressBar и создаем фильт по жанрам
        showProgressBar(false)
        setupGenresFilter(films)
    }

    fun setupGenresFilter(filmsList: List<Film>){// создаем фильтр по жанрам
        val allGenres: MutableList<String> = presenter.getAllGenres(filmsList).toMutableList()//получем список всех жанров
        if (!allGenres.isEmpty()){
            allGenres.add(0, "все")// добавляем на нулевую позицию жанр "все"
            var index = 0
            for(genre in allGenres){//создаем RadioButton для каждого жанра
                val genreRadioBtn = layoutInflater.inflate(R.layout.genre_item, radioGroup, false) as RadioButton
                genreRadioBtn.text = genre.replaceFirstChar { c -> c.uppercase() }
                genreRadioBtn.id = View.generateViewId()
                radioGroup.addView(genreRadioBtn, index++)//добавляем созданный на основе жанра RadioButton в RadioGroup
            }

            // в случе если жанр поменялся, то пересоздаем RecyclerView со списком фильмов соответсвующего жанра
            radioGroup.setOnCheckedChangeListener{ rg, id ->
                val genre: String = (rg.findViewById(id) as RadioButton).text.toString().lowercase()//получаем название выбранного жанра
                presenter.checkedGenre = genre // сохраняем название выбранного жанра в Presenter (чтобы оно пережило смену конфигурации)
                if (genre == "все")
                    setupRecyclerView(filmsList)// создаем RecyclerView на основе всех фильмов
                else
                    setupRecyclerView(presenter.getFilmsByGenre(filmsList, genre))// создаем RecyclerView на основе фильмов конкретного жанра
            }

            binding.genresFilterContainer.addView(radioGroup)// добавляем созданный radioGroup на layout

            if(presenter.checkedGenre == "") // проверям был ли ранее (перед пересозданием) уже выбран какой-то жанр
                (radioGroup.get(0) as RadioButton).isChecked = true // если нет то пусть первым выбранным элемнтом будет "все"
            else
                setGenreFilterItemChecked(presenter.checkedGenre)// иначе отмечаем ранее выбранный жанр
        }
    }

    fun setGenreFilterItemChecked(genre: String) {
        radioGroup.forEach { view ->//ищем radioButton с выбранным жанром и отмечаем его
            val radioButton = (view as RadioButton)
            if(radioButton.text.toString().lowercase() == genre)// сравниваем текст на radioButton с названем сохраненного жанра
                radioButton.isChecked = true
        }
    }

    fun setupRecyclerView(films: List<Film>){// создаем RecyclerView(
        binding.movieRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.movieRecyclerView.adapter = MovieRecyclerViewAdapter(films)
    }

    fun showProgressBar(boolean: Boolean){
        if(boolean){
            binding.progressBar.visibility = View.VISIBLE
            binding.movieRecyclerView.visibility = View.INVISIBLE
        }
        else{
            binding.progressBar.visibility = View.INVISIBLE
            binding.movieRecyclerView.visibility = View.VISIBLE
        }
    }


}
